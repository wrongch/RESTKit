package io.github.wrongch.restkit.toolwindow.action.toolbar;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import io.github.wrongch.restkit.config.EnvironmentConfigurable;
import org.jetbrains.annotations.NotNull;

/**
 * ShowEnvironmentSettingAction
 *
 * @author huzunrong
 * @since 2.0.0
 */
public class ShowEnvironmentSettingAction extends DumbAwareAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), EnvironmentConfigurable.class);
    }
}