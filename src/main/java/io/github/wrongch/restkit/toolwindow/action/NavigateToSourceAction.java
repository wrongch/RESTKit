package io.github.wrongch.restkit.toolwindow.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.util.PsiNavigateUtil;
import io.github.wrongch.restkit.common.PsiRestItem;
import io.github.wrongch.restkit.common.RestDataKey;
import io.github.wrongch.restkit.common.RestItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NavigateToSourceAction extends AnAction implements DumbAware {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        List<RestItem> items = RestDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        boolean match = items != null && items.stream()
                                              .allMatch(restItem -> restItem instanceof PsiRestItem);
        e.getPresentation().setVisible(match);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        List<RestItem> items = RestDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        if (items != null) {
            items.stream()
                 .filter(restItem -> restItem instanceof PsiRestItem)
                 .forEach(restItem -> {
                     PsiNavigateUtil.navigate(((PsiRestItem) restItem).getPsiElement());
                 });
        }
    }
}