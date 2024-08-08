package io.github.wrongch.restkit.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import io.github.wrongch.restkit.config.CommonSetting;
import io.github.wrongch.restkit.config.CommonSettingComponent;
import io.github.wrongch.restkit.util.FileUtils;
import io.github.wrongch.restkit.util.NotifierUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

import static io.github.wrongch.restkit.common.RestConstant.WEB_FRAMEWORK_LOCAL;

/**
 * open local api store file in editor
 *
 * @author huzunrong
 * @since 2.0.2
 */
public class OpenLocalApiFileAction extends DumbAwareAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        CommonSetting setting = CommonSettingComponent.getInstance(project).getState();
        e.getPresentation().setEnabled(
                setting.getEnabledWebFrameworks().contains(WEB_FRAMEWORK_LOCAL)
                && StringUtils.isNotEmpty(setting.getApiFilePath())
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        CommonSetting setting = CommonSettingComponent.getInstance(project).getState();
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(Paths.get(setting.getApiFilePath()));
        if (file != null) {
            VfsUtil.markDirtyAndRefresh(true, false, false, file);
            FileEditorManager.getInstance(project).openFile(file, true);
        } else {
            NotifierUtils.infoBalloon("Cannot find local api store file", FileUtils.getRestDirectory(project), null, project);
        }
    }
}
