package io.github.wrongch.restkit.toolwindow.action.toolbar;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.github.wrongch.restkit.toolwindow.RestToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * CollapseAllAction
 */
public class CollapseAllAction extends AnAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        RestToolWindowFactory.getRestServiceToolWindow(project, toolWindow -> toolWindow.expandAll(false));
    }
}
