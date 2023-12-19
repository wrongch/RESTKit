package io.github.wrongch.restkit.restful.ep;

import com.intellij.openapi.extensions.ExtensionPointName;
import io.github.wrongch.restkit.restful.LanguageResolver;
import org.jetbrains.annotations.NotNull;

/**
 * LanguageResolverProvider
 *
 * @author huzunrong
 * @since 2.0.1
 */
public interface LanguageResolverProvider {

    ExtensionPointName<LanguageResolverProvider> EP_NAME = ExtensionPointName.create("io.github.wrongch.restkit.languageResolver");

    @NotNull
    LanguageResolver createLanguageResolver();
}
