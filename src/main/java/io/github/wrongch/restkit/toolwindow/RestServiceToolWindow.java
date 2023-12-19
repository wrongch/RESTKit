package io.github.wrongch.restkit.toolwindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiElement;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import io.github.wrongch.restkit.common.RestItem;
import io.github.wrongch.restkit.util.IdeaUtils;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * RestServiceToolWindow
 */
public class RestServiceToolWindow extends SimpleToolWindowPanel {

    @Getter
    private final ToolWindow myToolWindow;
    private final Project myProject;

    private RestServiceTree restServiceTree;

    public RestServiceToolWindow(Project myProject, ToolWindow toolWindow) {
        super(true, true);

        this.myProject = myProject;
        this.myToolWindow = toolWindow;

        initToolWindow();
    }

    private void initToolWindow() {
        // 设置ToolWindow的顶部工具条
        final ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
                (DefaultActionGroup) actionManager.getAction("RESTKit.NavigatorActionsToolbar"),
                true);
        setToolbar(actionToolbar.getComponent());

        // 上下两部分：tree和RestServiceClient
        Splitter servicesContentPaneSplitter = new Splitter(true, .5f);
        servicesContentPaneSplitter.setShowDividerControls(true);
        servicesContentPaneSplitter.setDividerWidth(10);

        restServiceTree = new RestServiceTree(myProject);
        // 关系到获取dataContext
        actionToolbar.setTargetComponent(restServiceTree);

        servicesContentPaneSplitter.setFirstComponent(ScrollPaneFactory.createScrollPane(restServiceTree));
        servicesContentPaneSplitter.setSecondComponent(new RestServiceClient(myProject));
        setContent(servicesContentPaneSplitter);

        Content content = ApplicationManager.getApplication().getService(ContentFactory.class).createContent(this, "", false);
        ContentManager contentManager = myToolWindow.getContentManager();
        contentManager.addContent(content);
        contentManager.setSelectedContent(content, false);

        // update tree
        IdeaUtils.runWhenProjectIsReady(myProject, () -> restServiceTree.updateTree(null));
    }

    /**
     * 跳转到节点
     */
    public void navigateToTree(PsiElement psiElement) {
        navigateToTree(psiElement, null);
    }

    /**
     * 跳转到节点，节点不存在时可生成请求
     */
    public void navigateToTree(PsiElement psiElement, Supplier<RestItem> geneWhenNotExistNode) {
        if (myToolWindow.isDisposed() || !myToolWindow.isVisible()) {
            myToolWindow.show(() -> {
                restServiceTree.navigateToTree(psiElement, geneWhenNotExistNode);
            });
        } else {
            restServiceTree.navigateToTree(psiElement, geneWhenNotExistNode);
        }
    }

    /**
     * 跳转到节点
     */
    public void navigateToTree(String url, String method, String moduleName) {
        if (myToolWindow.isDisposed() || !myToolWindow.isVisible()) {
            myToolWindow.show(() -> {
                restServiceTree.navigateToTree(url, method, moduleName);
            });
        } else {
            restServiceTree.navigateToTree(url, method, moduleName);
        }
    }

    public void scheduleUpdateTree() {
        IdeaUtils.runWhenProjectIsReady(myProject, () -> {
            if (myToolWindow.isDisposed() || !myToolWindow.isVisible()) {
                myToolWindow.show(() -> {
                    restServiceTree.updateTree(null);
                });
            } else {
                restServiceTree.updateTree(null);
            }
        });
    }

    public void expandAll(boolean expand) {
        restServiceTree.expandAll(expand);
    }
}
