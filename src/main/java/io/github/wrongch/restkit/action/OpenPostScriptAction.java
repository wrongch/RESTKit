package io.github.wrongch.restkit.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import org.jetbrains.annotations.NotNull;

/**
 * open post-request script in editor
 *
 * @author huzunrong
 * @since 2.0.1
 */
public class OpenPostScriptAction extends OpenPreScriptAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    protected boolean isPreScript() {
        return false;
    }
}
