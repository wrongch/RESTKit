package io.github.newhoo.restkit.restful.http;

import com.intellij.openapi.project.Project;
import io.github.newhoo.restkit.common.KV;
import io.github.newhoo.restkit.common.Request;
import io.github.newhoo.restkit.common.RequestInfo;
import io.github.newhoo.restkit.common.RestClientData;
import io.github.newhoo.restkit.common.RestItem;
import io.github.newhoo.restkit.config.CommonSettingComponent;
import io.github.newhoo.restkit.config.Environment;
import io.github.newhoo.restkit.restful.RestClient;
import io.github.newhoo.restkit.restful.ep.RestClientProvider;
import io.github.newhoo.restkit.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.newhoo.restkit.common.RestConstant.PROTOCOL_HTTP;

/**
 * http client
 *
 * @author huzunrong
 * @date 2022/3/12 2:12 PM
 * @since 2.0.3
 */
public class HttpClient implements RestClient {

    @NotNull
    @Override
    public String getProtocol() {
        return PROTOCOL_HTTP;
    }

    @Override
    public List<KV> getConfig(@NotNull RestItem restItem, @NotNull Project project) {
        int timeout = CommonSettingComponent.getInstance(project).getState().getRequestTimeout();
        // 默认最大设置60s
        if (timeout <= 0) {
            timeout = 60000;
        }
        List<KV> list = new LinkedList<>();
        list.add(new KV("baseUrl", "{{baseUrl}}"));
        list.add(new KV("timeout", String.valueOf(timeout)));

        Map<String, String> env = Environment.getInstance(project).getCurrentEnabledEnvMap();
        String p12Path = env.get("p12Path");
        String p12Passwd = env.get("p12Passwd");
        // 双向认证
        if (!StringUtils.isAnyEmpty(p12Path, p12Passwd)) {
            list.add(new KV("p12Path", "{{p12Path}}"));
            list.add(new KV("p12Passwd", "{{p12Passwd}}"));
        }
        return list;
    }

    @NotNull
    @Override
    public Request createRequest(RestClientData restClientData, Project project) {
        Map<String, String> config = restClientData.getConfig();
        String url = StringUtils.defaultString(restClientData.getUrl());
        if (!url.contains("://")) {
            if (!url.startsWith("/")) {
                url = "/" + url;
            }
            url = StringUtils.defaultIfEmpty(config.get("baseUrl"), "http://localhost:8080") + url;
            // 环境变量未设置【baseUrl】时强行替换为localhost:8080
            url = url.replace("{{baseUrl}}", "http://localhost:8080");
        }
        // 移除未设置的证书
        if ("{{p12Path}}".equals(config.get("p12Path"))) {
            config.remove("p12Path");
        }
        if ("{{p12Passwd}}".equals(config.get("p12Passwd"))) {
            config.remove("p12Passwd");
        }

        HttpRequest request = new HttpRequest();
        request.setUrl(url);
        request.setMethod(restClientData.getMethod());
        request.setConfig(config);
        request.setHeaders(restClientData.getHeaders());
        request.setParams(restClientData.getParams());
        request.setBody(restClientData.getBody());
        return request;
    }

    @NotNull
    @Override
    public RequestInfo sendRequest(Request request, Project project) {
        return HttpUtils.request((HttpRequest) request);
    }

    @NotNull
    @Override
    public String formatResponseInfo(RequestInfo requestInfo) {
        HttpRequest request = (HttpRequest) requestInfo.getRequest();
        HttpResponse response = (HttpResponse) requestInfo.getResponse();

        StringBuilder sb = new StringBuilder();

        String status = "ERROR";
        if (response != null && response.getOriginal() != null) {
            StatusLine statusLine = response.getOriginal().getStatusLine();
            status = statusLine.getStatusCode() + " " + StringUtils.defaultString(statusLine.getReasonPhrase());
        }
        sb.append("Status: ").append(status).append("    ").append("Time: ").append(requestInfo.getCost()).append("ms").append("\n")
          .append("Remote Address: ").append(requestInfo.getRemoteAddress()).append("\n")
          .append("------------------------------------\n");

        if (request.getOriginal() != null) {
            sb.append(request.getOriginal().getRequestLine()).append("\n");
            List<Header> collect = Arrays.stream(request.getOriginal().getAllHeaders())
                                         .collect(Collectors.toList());
            if (request.getOriginal() instanceof HttpEntityEnclosingRequest){
                HttpEntity entity = ((HttpEntityEnclosingRequest) request.getOriginal()).getEntity();
                collect.add(entity.getContentType());
                collect.add(new BasicHeader("Content-Length", String.valueOf(entity.getContentLength())));
            }
            String reqHeader = formatHeader(collect.toArray(new Header[0]));
            // String reqHeader = formatHeader(request.getOriginal().getAllHeaders());
            if (StringUtils.isNotEmpty(reqHeader)) {
                sb.append(reqHeader).append("\n");
            }
            if (StringUtils.isNotEmpty(request.getBody())) {
                sb.append("\n").append(request.getBody()).append("\n");
            }
        }

        if (response != null && response.getOriginal() != null) {
            sb.append("\n");
            sb.append(response.getOriginal().getStatusLine()).append("\n");
            String respHeader = formatHeader(response.getOriginal().getAllHeaders());
            if (StringUtils.isNotEmpty(respHeader)) {
                sb.append(respHeader).append("\n");
            }
            if (StringUtils.isNotEmpty(response.getBody0())) {
                // 替换response内容\r\n的\r
                sb.append("\n").append(response.getBody0().replace("\r", "")).append("\n");
            }
        }
        return sb.toString();
    }

    @NotNull
    @Override
    public String formatLogInfo(RequestInfo requestInfo) {
        HttpRequest request = (HttpRequest) requestInfo.getRequest();
        HttpResponse response = (HttpResponse) requestInfo.getResponse();

        StringBuilder sb = new StringBuilder();
        sb.append("############################# ").append(LocalDateTime.now()).append(" #############################").append("\n");
        String status = "ERROR";
        if (response != null && response.getOriginal() != null) {
            StatusLine statusLine = response.getOriginal().getStatusLine();
            status = statusLine.getStatusCode() + " " + StringUtils.defaultString(statusLine.getReasonPhrase());
        }
        sb.append("Status: ").append(status).append("    ")
          .append("Time: ").append(requestInfo.getCost()).append("ms").append("    ")
          .append("Remote Address: ").append(requestInfo.getRemoteAddress()).append("\n\n");

        if (request.getOriginal() != null) {
            sb.append(">>> ").append(request.getOriginal().getRequestLine()).append("\n");
            List<Header> collect = Arrays.stream(request.getOriginal().getAllHeaders())
                                         .collect(Collectors.toList());
            if (request.getOriginal() instanceof HttpEntityEnclosingRequest){
                HttpEntity entity = ((HttpEntityEnclosingRequest) request.getOriginal()).getEntity();
                collect.add(entity.getContentType());
                collect.add(new BasicHeader("Content-Length", String.valueOf(entity.getContentLength())));
            }
            String reqHeader = formatHeader(collect.toArray(new Header[0]));
            // String reqHeader = formatHeader(request.getOriginal().getAllHeaders());
            if (StringUtils.isNotEmpty(reqHeader)) {
                sb.append(reqHeader).append("\n");
            }
            if (StringUtils.isNotEmpty(request.getBody())) {
                sb.append("\n").append(request.getBody()).append("\n");
            }
        }
        if (response != null && response.getOriginal() != null) {
            sb.append("\n");
            sb.append("<<< ").append(response.getOriginal().getStatusLine()).append("\n");
            String respHeader = formatHeader(response.getOriginal().getAllHeaders());
            if (StringUtils.isNotEmpty(respHeader)) {
                sb.append(respHeader).append("\n");
            }
            if (StringUtils.isNotEmpty(response.getBody())) {
                sb.append("\n").append(response.getBody()).append("\n");
            }
        }
        if (StringUtils.isNotEmpty(requestInfo.getErrMsg())) {
            sb.append("\n");
            sb.append("<<< ERROR").append("\n");
            sb.append(requestInfo.getErrMsg()).append("\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }

    private String formatHeader(Header[] headers) {
        return Arrays.stream(headers).map(Object::toString).collect(Collectors.joining("\n"));
    }

    public static class HttpClientProvider implements RestClientProvider {
        @Override
        public RestClient createClient() {
            return new HttpClient();
        }
    }
}
