package io.github.wrongch.restkit.restful.ep;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import io.github.wrongch.restkit.restful.RequestResolver;
import org.jetbrains.annotations.NotNull;

/**
 * RestfulResolverProvider
 *
 * @author huzunrong
 * @since 2.0.0
 */
public interface RestfulResolverProvider {

    ExtensionPointName<RestfulResolverProvider> EP_NAME = ExtensionPointName.create("io.github.wrongch.restkit.restfulResolver");

    RequestResolver createRequestResolver(@NotNull Project project);
}
