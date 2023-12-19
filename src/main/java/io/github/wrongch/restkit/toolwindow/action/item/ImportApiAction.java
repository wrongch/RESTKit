package io.github.wrongch.restkit.toolwindow.action.item;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.messages.MessagesService;
import io.github.wrongch.restkit.common.RestItem;
import io.github.wrongch.restkit.config.SettingConfigurable;
import io.github.wrongch.restkit.i18n.RestBundle;
import io.github.wrongch.restkit.restful.RequestHelper;
import io.github.wrongch.restkit.restful.RequestResolver;
import io.github.wrongch.restkit.toolwindow.RestServiceToolWindow;
import io.github.wrongch.restkit.toolwindow.RestToolWindowFactory;
import io.github.wrongch.restkit.util.HtmlUtil;
import io.github.wrongch.restkit.util.JsonUtils;
import io.github.wrongch.restkit.util.NotifierUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ImportApiAction
 *
 * @author huzunrong
 * @since 2.0.1
 */
public class ImportApiAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        Transferable contents = CopyPasteManager.getInstance().getContents();
        if (contents == null) {
            return;
        }

        Map<String, RequestResolver> resolverMap = RequestHelper.getEnabledRequestResolvers(project)
                                                                .stream()
                                                                .filter(o -> o.getScanType() == RequestResolver.ScanType.STORAGE)
                                                                .collect(Collectors.toMap(RequestResolver::getFrameworkName, o -> o, (o1, o2) -> o1));
        if (resolverMap.isEmpty()) {
            NotifierUtils.infoBalloon("", "Supported store frameworks disabled. " + HtmlUtil.link("Edit", "Edit"), new NotificationListener.Adapter() {
                @Override
                protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent e) {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, SettingConfigurable.class);
                }
            }, project);
            return;
        }
        String[] frameworks = new ArrayList<>(resolverMap.keySet()).toArray(new String[0]);
        int index = MessagesService.getInstance().showChooseDialog(project, null, "Select enabled store framework", "Api Importing Destination", frameworks, frameworks[0], null);
        if (index < 0) {
            return;
        }
        RequestResolver requestResolver = resolverMap.get(frameworks[index]);

        try {
            String data = contents.getTransferData(DataFlavor.stringFlavor).toString();
            List<RestItem> restItems = JsonUtils.fromJsonArr(data, RestItem.class);
            if (restItems != null) {
                ProgressManager.getInstance().run(new Task.Backgroundable(project, "[RESTKit] Import api") {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        restItems.forEach(o -> {
                            o.setUrl(StringUtils.defaultString(o.getUrl()));
                            o.setBodyJson(StringUtils.defaultString(o.getBodyJson()));
                            o.setDescription(StringUtils.defaultString(o.getDescription()));
                            o.setFramework(requestResolver.getFrameworkName());
                            o.setModuleName(o.getModuleName() + "(import)");
                            o.setTs(System.currentTimeMillis());
                        });
                        requestResolver.add(restItems);
                        RestToolWindowFactory.getRestServiceToolWindow(project, RestServiceToolWindow::scheduleUpdateTree);
                    }
                });
            } else {
                NotifierUtils.errorBalloon(RestBundle.message("toolkit.local.api.import.title"), RestBundle.message("toolkit.local.api.import.content", "format error"), project);
            }
        } catch (Exception ex) {
            NotifierUtils.errorBalloon(RestBundle.message("toolkit.local.api.import.title"), RestBundle.message("toolkit.local.api.import.content", ex.toString()), project);
        }
    }
}
