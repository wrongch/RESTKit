package io.github.wrongch.restkit.feature.javaimpl.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiElement;
import io.github.wrongch.restkit.config.CommonSettingComponent;
import io.github.wrongch.restkit.restful.LanguageHelper;
import io.github.wrongch.restkit.toolwindow.RestToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * JumpToTreeAction
 *
 * @author huzunrong
 * @since 2.0.0
 */
public class JumpToTreeAction extends AnAction {

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        boolean bool;
        if (psiElement == null
                || CommonSettingComponent.getInstance(psiElement.getProject()).getState().isEnableMethodLineMarker()) {
            bool = false;
        } else {
            bool = LanguageHelper.canNavigateToTree(psiElement);
        }
        e.getPresentation().setEnabledAndVisible(bool);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiElement psiElement = e.getRequiredData(CommonDataKeys.PSI_ELEMENT);
        RestToolWindowFactory.getRestServiceToolWindow(e.getProject(), restServiceToolWindow -> {
            restServiceToolWindow.navigateToTree(psiElement, () -> LanguageHelper.generateRestItem(psiElement));
        });
    }
}
