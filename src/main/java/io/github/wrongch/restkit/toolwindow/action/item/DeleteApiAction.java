package io.github.wrongch.restkit.toolwindow.action.item;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import io.github.wrongch.restkit.common.RestDataKey;
import io.github.wrongch.restkit.common.RestItem;
import io.github.wrongch.restkit.restful.RequestHelper;
import io.github.wrongch.restkit.restful.RequestResolver;
import io.github.wrongch.restkit.toolwindow.RestServiceToolWindow;
import io.github.wrongch.restkit.toolwindow.RestToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DeleteApiAction
 *
 * @author huzunrong
 * @since 2.0.1
 */
public class DeleteApiAction extends AnAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        List<RestItem> items = RestDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        if (project == null || items == null) {
            return;
        }
        e.getPresentation().setVisible(items.stream().allMatch(RestItem::canDelete));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        List<RestItem> items = RestDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        if (items != null) {
            int yesNoDialog = Messages.showYesNoDialog(project, "Delete " + items.size() + " api?", "Delete", null);
            if (Messages.YES != yesNoDialog) {
                return;
            }
            Map<String, RequestResolver> resolverMap = RequestHelper.getAllRequestResolvers(project)
                                                                    .stream()
                                                                    .collect(Collectors.toMap(RequestResolver::getFrameworkName, o -> o));
            items.stream()
                 .collect(Collectors.groupingBy(RestItem::getFramework))
                 .entrySet()
                 .stream()
                 .filter(entry -> resolverMap.containsKey(entry.getKey()))
                 .forEach(entry -> {
                     resolverMap.get(entry.getKey()).delete(entry.getValue());
                 });
            RestToolWindowFactory.getRestServiceToolWindow(project, RestServiceToolWindow::scheduleUpdateTree);
        }
    }
}
