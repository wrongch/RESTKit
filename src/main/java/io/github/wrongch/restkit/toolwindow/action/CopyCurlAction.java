package io.github.wrongch.restkit.toolwindow.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.SystemInfo;
import io.github.wrongch.restkit.common.HttpMethod;
import io.github.wrongch.restkit.common.RestClientApiInfo;
import io.github.wrongch.restkit.common.RestDataKey;
import io.github.wrongch.restkit.config.HttpSetting;
import io.github.wrongch.restkit.config.HttpSettingComponent;
import io.github.wrongch.restkit.config.certificate.Certificate;
import io.github.wrongch.restkit.config.certificate.CertificateComponent;
import io.github.wrongch.restkit.util.EnvironmentUtils;
import io.github.wrongch.restkit.util.IdeaUtils;
import io.github.wrongch.restkit.util.NotifierUtils;
import io.github.wrongch.restkit.util.ToolkitUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.wrongch.restkit.common.RestConstant.HTTP_BASE_URL_DEFAULT;
import static io.github.wrongch.restkit.common.RestConstant.HTTP_BASE_URL_PLACEHOLDER;
import static io.github.wrongch.restkit.common.RestConstant.HTTP_FILE_PREFIX;
import static io.github.wrongch.restkit.common.RestConstant.HTTP_URL_HTTP;
import static io.github.wrongch.restkit.common.RestConstant.HTTP_URL_HTTPS;
import static io.github.wrongch.restkit.common.RestConstant.PROTOCOL;
import static io.github.wrongch.restkit.common.RestConstant.PROTOCOL_HTTP;

/**
 * CopyCurlAction
 *
 * @author huzunrong
 * @since 2.0.1
 */
public class CopyCurlAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        RestClientApiInfo apiInfo = RestDataKey.CLIENT_API_INFO.getData(e.getDataContext());
        if (project == null || apiInfo == null) {
            e.getPresentation().setVisible(false);
            return;
        }
        Map<String, String> configMap = ToolkitUtil.textToModifiableMap(apiInfo.getConfig());
        if (configMap.containsKey(PROTOCOL) && !PROTOCOL_HTTP.equals(configMap.get(PROTOCOL))) {
            e.getPresentation().setVisible(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        RestClientApiInfo apiInfo = RestDataKey.CLIENT_API_INFO.getData(e.getDataContext());
        if (project == null || apiInfo == null) {
            return;
        }
        HttpMethod httpMethod = apiInfo.getMethod();
        if (httpMethod == null || httpMethod == HttpMethod.UNDEFINED) {
            return;
        }

        // header
        Map<String, String> headerMap = ToolkitUtil.textToModifiableMap(EnvironmentUtils.handlePlaceholderVariable(apiInfo.getHeaders(), project));
        if (HttpMethod.GET != httpMethod && StringUtils.isNotEmpty(apiInfo.getBodyJson())) {
            headerMap.putIfAbsent("Content-Type", "application/json;charset=UTF-8");
        }

        String url = apiInfo.getUrl();
        if (!url.contains("://")) {
            url = EnvironmentUtils.handlePlaceholderVariable(HTTP_BASE_URL_PLACEHOLDER + url, project);
            // 环境变量未设置【baseUrl】时强行替换为localhost:8080
            url = url.replace(HTTP_BASE_URL_PLACEHOLDER, HTTP_BASE_URL_DEFAULT);
        }
        if (!url.startsWith(HTTP_URL_HTTP) && !url.startsWith(HTTP_URL_HTTPS)) {
            url = HTTP_URL_HTTP + url;
        }
        // 自带的query参数编码
        if (url.contains("?")) {
            String[] split = StringUtils.split(url, "?", 2);
            url = split[0] + "?" + ToolkitUtil.encodeQueryParam(split[1]);
        }

        Map<String, String> paramMap = ToolkitUtil.textToModifiableMap(EnvironmentUtils.handlePlaceholderVariable(apiInfo.getParams(), project));
        // file params
        Map<String, String> fileParamsMap = paramMap.entrySet()
                                                    .stream()
                                                    .filter(entry -> StringUtils.startsWith(entry.getValue(), HTTP_FILE_PREFIX))
                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // path variables
        Set<String> pathVariables = new HashSet<>(4);
        // 替换URL
        if (url.contains("{") && url.contains("}") && !paramMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                if (StringUtils.isNotEmpty(entry.getValue()) && !fileParamsMap.containsKey(entry.getKey()) && url.contains(placeholder)) {
                    pathVariables.add(entry.getKey());
                    url = url.replace(placeholder, entry.getValue());
//                    url = url.replaceFirst("\\{(" + key + "[\\s\\S]*?)}", v);
                }
            }
        }
        // query/form params
        Map<String, String> queryOrFormParamsMap = paramMap.entrySet()
                                                           .stream()
                                                           .filter(entry -> !fileParamsMap.containsKey(entry.getKey()))
                                                           .filter(entry -> !pathVariables.contains(entry.getKey()))
                                                           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // query params
        if (HttpMethod.GET == httpMethod || StringUtils.isNotEmpty(apiInfo.getBodyJson())) {
            fileParamsMap.clear();
            if (!queryOrFormParamsMap.isEmpty()) {
                String params = ToolkitUtil.getRequestParam(queryOrFormParamsMap);
                // URL可能包含了参数
                url += url.contains("?") ? "&" + params : "?" + params;
            }
        }

        String p12Path = null, p12Passwd = null;
        if (url.startsWith(HTTP_URL_HTTPS)) {
            String tmpUrl = url.substring(8);
            String host = tmpUrl.contains("/") ? tmpUrl.substring(0, tmpUrl.indexOf("/")) : tmpUrl;
            Certificate cert = CertificateComponent.getInstance().getEnabledCertificate(host);
            if (cert != null) {
                p12Path = cert.getPfxFile();
                p12Passwd = cert.getPassphrase();
            }
        }


        // not windows
        HttpSetting httpSetting = HttpSettingComponent.getInstance(project).getState();
        if (!SystemInfo.isWindows || !httpSetting.isSupportForWslPath()) {
            doCopyCurl(url, httpMethod, headerMap,
                    fileParamsMap, queryOrFormParamsMap, apiInfo.getBodyJson(),
                    p12Path, p12Passwd, false, httpSetting.isGenerateMultilineCurlSnippet(), project);
            return;
        }

        String finalUrl = url;
        String finalP12Path = p12Path;
        String finalP12Passwd = p12Passwd;
        List<AnAction> actions = new ArrayList<>(4);
        actions.add(new AnAction("Copy") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                doCopyCurl(finalUrl, httpMethod, headerMap,
                        fileParamsMap, queryOrFormParamsMap, apiInfo.getBodyJson(),
                        finalP12Path, finalP12Passwd, false, httpSetting.isGenerateMultilineCurlSnippet(), project);
            }
        });
        if (!StringUtils.isAnyEmpty(p12Path, p12Passwd)
                || (HttpMethod.GET != httpMethod && StringUtils.isEmpty(apiInfo.getBodyJson()) && !fileParamsMap.isEmpty())) {
            actions.add(new AnAction("Copy for Wsl") {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    doCopyCurl(finalUrl, httpMethod, headerMap,
                            fileParamsMap, queryOrFormParamsMap, apiInfo.getBodyJson(),
                            finalP12Path, finalP12Passwd, true, httpSetting.isGenerateMultilineCurlSnippet(), project);
                }
            });
        }

        if (actions.size() == 1) {
            actions.get(0).actionPerformed(e);
            return;
        }

        final ListPopup popup = JBPopupFactory.getInstance()
                                              .createActionGroupPopup(
                                                      null,
                                                      new DefaultActionGroup(actions),
                                                      e.getDataContext(),
                                                      JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                                                      true);
        popup.showInBestPositionFor(e.getDataContext());
    }

    private void doCopyCurl(String url,
                            HttpMethod httpMethod,
                            Map<String, String> headerMap,
                            Map<String, String> fileParamsMap,
                            Map<String, String> queryOrFormParamsMap,
                            String bodyJson,
                            String p12Path, String p12Passwd,
                            boolean wsl, boolean supportMultiline,
                            Project project
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("curl ");
        sb.append("-X").append(" ").append(httpMethod.name()).append(" ");
        sb.append(url).append(" ").append(appendMultilineStr(supportMultiline));

        headerMap.forEach((k, v) -> {
            sb.append("-H").append(" ").append("'").append(k).append(": ").append(v).append("'").append(" ").append(appendMultilineStr(supportMultiline));
        });

        if (HttpMethod.GET != httpMethod) {
            if (StringUtils.isNotEmpty(bodyJson)) {
                sb.append("-d '").append(bodyJson).append("'").append(" ").append(appendMultilineStr(supportMultiline));
            } else {
                // form params: Content-Type: application/x-www-form-urlencoded
                if (fileParamsMap.isEmpty()) {
                    queryOrFormParamsMap.forEach((k, v) -> {
                        sb.append("-d '").append(k).append("=").append(v).append("'").append(" ").append(appendMultilineStr(supportMultiline));
                    });
                } else {
                    // form params: Content-Type: multipart/form-data; boundary=------------------------75a1b524af201d5c
                    queryOrFormParamsMap.forEach((k, v) -> {
                        sb.append("-F '").append(k).append("=").append(v).append("'").append(" ").append(appendMultilineStr(supportMultiline));
                    });
                    fileParamsMap.forEach((k, v) -> {
                        sb.append("-F '").append(k).append("=@\"").append(getFilepath(ToolkitUtil.getUploadFilepath(v), wsl)).append("\"'").append(" ").append(appendMultilineStr(supportMultiline));
                    });
                }
            }
        }

        if (url.startsWith(HTTP_URL_HTTPS)) {
            sb.append("-k ");

            // 双向认证
            if (!StringUtils.isAnyEmpty(p12Path, p12Passwd)) {
                sb.append("--cert-type P12 --cert ").append("'").append(getFilepath(p12Path, wsl)).append("'").append(":").append("'").append(p12Passwd).append("'");
            }
        }

        String s = sb.toString();
        if (s.endsWith("\\\n")) {
            s = s.substring(0, s.length() - 2);
        }

        IdeaUtils.copyToClipboard(s);
        NotifierUtils.infoBalloon("", "Curl copied to clipboard successfully.", null, project);
    }

    private String appendMultilineStr(boolean supportMultiline) {
        return supportMultiline ? "\\\n" : "";
    }

    private String getFilepath(String filepath, boolean wsl) {
        if (wsl && StringUtils.contains(filepath, ':')) {
            filepath = filepath.substring(0, 1).toLowerCase() + filepath.substring(1);
            filepath = "/mnt/" + filepath.replace(":", "").replace('\\', '/');
        }
        return filepath;
    }
}
