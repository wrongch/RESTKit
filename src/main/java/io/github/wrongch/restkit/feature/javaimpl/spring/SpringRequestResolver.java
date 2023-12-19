package io.github.wrongch.restkit.feature.javaimpl.spring;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.wrongch.restkit.common.RestItem;
import io.github.wrongch.restkit.restful.BaseRequestResolver;
import io.github.wrongch.restkit.restful.LanguageHelper;
import io.github.wrongch.restkit.restful.RequestResolver;
import io.github.wrongch.restkit.restful.ep.RestfulResolverProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.wrongch.restkit.common.RestConstant.WEB_FRAMEWORK_SPRING_MVC;

/**
 * resolve restful apis in Spring MVC
 */
public class SpringRequestResolver extends BaseRequestResolver {

    @NotNull
    @Override
    public String getFrameworkName() {
        return WEB_FRAMEWORK_SPRING_MVC;
    }

    @Override
    public List<RestItem> findRestItemListInModule(Module module, GlobalSearchScope globalSearchScope) {
        return LanguageHelper.LANGUAGE_RESOLVER_MAP.values()
                                                   .stream()
                                                   .filter(resolver -> resolver instanceof BaseRequestResolver)
                                                   .map(resolver -> ((BaseRequestResolver) resolver).findRestItemListInModule(module, globalSearchScope))
                                                   .flatMap(Collection::stream)
                                                   .collect(Collectors.toList());
    }

    public static class SpringRequestResolverProvider implements RestfulResolverProvider {

        @Override
        public RequestResolver createRequestResolver(@NotNull Project project) {
            return new SpringRequestResolver();
        }
    }
}
