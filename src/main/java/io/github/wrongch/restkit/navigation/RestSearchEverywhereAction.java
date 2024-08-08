package io.github.wrongch.restkit.navigation;

import com.intellij.ide.actions.SearchEverywhereBaseAction;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * RestSearchEverywhereAction
 *
 * @author huzunrong
 * @since 1.0.8
 */
public class RestSearchEverywhereAction extends SearchEverywhereBaseAction {


    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String tabID = RestSearchEverywhereContributor.class.getSimpleName();
        showInSearchEverywherePopup(tabID, e, true, true);
    }
}
