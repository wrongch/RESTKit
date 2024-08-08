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
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

/**
 * open request log in editor
 *
 * @author huzunrong
 * @since 2.0.1
 */
public class OpenRequestLogAction extends DumbAwareAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        CommonSetting setting = CommonSettingComponent.getInstance(project).getState();
        e.getPresentation().setEnabled(setting.isSaveRequestLog());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        String todayLog = FileUtils.getTodayLog(project);
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(Paths.get(todayLog));
        if (file != null) {
            VfsUtil.markDirtyAndRefresh(true, false, false, file);
            FileEditorManager.getInstance(project).openFile(file, true);
        } else {
            NotifierUtils.infoBalloon("Cannot find today's request log", FileUtils.getRestDirectory(project), null, project);
        }
    }
}
