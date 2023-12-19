package io.github.wrongch.restkit.common;

import com.intellij.icons.AllIcons.Nodes;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class ToolkitIcons {

    public static final Icon ROOT_MODULE = Nodes.ModuleGroup;
    public static final Icon PROJECT_MODULE = Nodes.Module;
    public static final Icon SERVICE = IconLoader.getIcon("/icons/service.png", ToolkitIcons.class); // services.svg/toolWindowServices.svg
    public static final Icon REQUEST = IconLoader.getIcon("/icons/request.svg", ToolkitIcons.class); // #4A86E8 -- related.svg

    public static class METHOD {
        public static Icon get(HttpMethod method) {
            if (method == null) {
                return UNDEFINED;
            }
            switch (method) {
                case GET:
                    return GET;
                case POST:
                    return POST;
                case PUT:
                    return PUT;
                case PATCH:
                    return PATCH;
                case DELETE:
                    return DELETE;
                default:
                    return UNDEFINED;
            }
        }

        private static final Icon GET = IconLoader.getIcon("/icons/method/get.png", ToolkitIcons.class); // 16x16 GREEN -- groovyLanguage.svg
        private static final Icon PUT = IconLoader.getIcon("/icons/method/put.png", ToolkitIcons.class); // 16x16 ORANGE -- pointcut.svg/parameter.svg
        private static final Icon POST = IconLoader.getIcon("/icons/method/post.png", ToolkitIcons.class); // 16x16 BLUE
        private static final Icon PATCH = IconLoader.getIcon("/icons/method/patch.png", ToolkitIcons.class); // 16x16 GRAY
        private static final Icon DELETE = IconLoader.getIcon("/icons/method/delete.png", ToolkitIcons.class); // 16x16 RED
        // OPTIONS HEAD
        private static final Icon UNDEFINED = IconLoader.getIcon("/icons/method/undefined.png", ToolkitIcons.class); // 16x16 GRAY -- emptyFatalError_dark.svg
    }
}
