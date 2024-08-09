package io.github.wrongch.restkit.feature.javaimpl.language;

import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.module.Module;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.javadoc.PsiDocToken;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.wrongch.restkit.common.KV;
import io.github.wrongch.restkit.common.RestItem;
import io.github.wrongch.restkit.feature.javaimpl.MethodPath;
import io.github.wrongch.restkit.feature.javaimpl.helper.PsiAnnotationHelper;
import io.github.wrongch.restkit.feature.javaimpl.helper.PsiClassHelper;
import io.github.wrongch.restkit.feature.javaimpl.spring.SpringAnnotationHelper;
import io.github.wrongch.restkit.feature.javaimpl.spring.SpringControllerAnnotation;
import io.github.wrongch.restkit.feature.javaimpl.spring.SpringRequestMethodAnnotation;
import io.github.wrongch.restkit.restful.LanguageResolver;
import io.github.wrongch.restkit.restful.ep.LanguageResolverProvider;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * JavaLanguageResolver, will work when Java enabled
 *
 * @author huzunrong
 * @since 2.0.0
 */
public class JavaLanguageResolver extends BaseLanguageResolver {

    @NotNull
    @Override
    public Language getLanguage() {
        return JavaLanguage.INSTANCE;
    }

    @Override
    public boolean canConvertToJSON(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiClass;
    }

    @Override
    public String convertToJSON(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PsiClass) {
            return PsiClassHelper.convertClassToJSON(((PsiClass) psiElement).getQualifiedName(), psiElement.getProject());
        }
        return null;
    }

    @Override
    public boolean canNavigateToTree(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiMethod
                && Arrays.stream(((PsiMethod) psiElement).getAnnotations())
                         .anyMatch(psiAnnotation -> SpringRequestMethodAnnotation.getByQualifiedName(psiAnnotation.getQualifiedName()) != null);
    }

    @Override
    public boolean canGenerateLineMarker(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiIdentifier
                && canNavigateToTree(psiElement.getParent());
    }

    @Override
    public RestItem tryGenerateRestItem(@NotNull PsiElement psiElement) {
        PsiMethod psiMethod;
        if (psiElement instanceof PsiMethod) {
            psiMethod = (PsiMethod) psiElement;
        } else if (psiElement.getParent() instanceof PsiMethod) {
            psiMethod = (PsiMethod) psiElement.getParent();
        } else {
            return null;
        }
        List<MethodPath> typeMethodPaths = SpringAnnotationHelper.getTypeMethodPaths(psiMethod.getContainingClass());
        List<MethodPath> methodMethodPaths = SpringAnnotationHelper.getMethodMethodPaths(psiMethod);
        return combineFirstRestItem(typeMethodPaths, methodMethodPaths, psiMethod, "");
    }

    @Override
    public List<RestItem> findRestItemListInModule(Module module, GlobalSearchScope globalSearchScope) {
        List<RestItem> itemList = new ArrayList<>();
        for (SpringControllerAnnotation ann : SpringControllerAnnotation.values()) {
            // java: 标注了 (Rest)Controller 注解的类，即 Controller 类
            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(ann.getShortName(), module.getProject(), globalSearchScope);
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                PsiElement psiElement = psiModifierList.getParent();

                if (psiElement instanceof PsiClass) {
                    PsiClass psiClass = (PsiClass) psiElement;
                    List<RestItem> serviceItemList = getRequestItemList(psiClass, module);
                    itemList.addAll(serviceItemList);
                }
            }
        }
        return itemList;
    }

    private List<RestItem> getRequestItemList(PsiClass psiClass, Module module) {
        List<PsiMethod> psiMethods = new ArrayList<>(Arrays.asList(psiClass.getMethods()));
        for (PsiClass aSuper : psiClass.getSupers()) {
            if (!"java.lang.Object".equals(aSuper.getQualifiedName())) {
                psiMethods.addAll(Arrays.asList(aSuper.getMethods()));
            }
        }
        if (psiMethods.isEmpty()) {
            return Collections.emptyList();
        }

        List<RestItem> itemList = new ArrayList<>();
        List<MethodPath> typeMethodPaths = SpringAnnotationHelper.getTypeMethodPaths(psiClass);

        for (PsiMethod psiMethod : psiMethods) {
            List<MethodPath> methodMethodPaths = SpringAnnotationHelper.getMethodMethodPaths(psiMethod);
            itemList.addAll(combineTypeAndMethod(typeMethodPaths, methodMethodPaths, psiMethod, module));
        }
        return itemList;
    }

    @NotNull
    @Override
    public List<KV> buildHeaders(@NotNull PsiElement psiElement) {
        if (!(psiElement instanceof PsiMethod psiMethod)) {
            return Collections.emptyList();
        }
        return buildHeaderString(psiMethod);
    }

    @NotNull
    @Override
    public List<KV> buildParams(@NotNull PsiElement psiElement) {
        if (!(psiElement instanceof PsiMethod psiMethod)) {
            return Collections.emptyList();
        }
        return buildParamString(psiMethod);
    }

    @NotNull
    @Override
    public String buildRequestBodyJson(@NotNull PsiElement psiElement) {
        if (!(psiElement instanceof PsiMethod psiMethod)) {
            return "";
        }
        String s = buildRequestBodyJson(psiMethod);
        return Objects.nonNull(s) ? s : "";
    }

    @NotNull
    @Override
    public String buildDescription(@NotNull PsiElement psiElement) {
        if (!(psiElement instanceof PsiMethod psiMethod)) {
            return "";
        }

        String restDoc = Optional.ofNullable(psiMethod.getAnnotation("io.swagger.annotations.ApiOperation"))
                .map(ann -> PsiAnnotationHelper.getAnnotationValue(ann, "value"))
                .orElse(null);

        if (restDoc == null && psiMethod.getDocComment() != null) {
            restDoc = Arrays.stream(psiMethod.getDocComment().getDescriptionElements())
                             .filter(e -> e instanceof PsiDocToken)
                             .filter(e -> StringUtils.isNotBlank(e.getText()))
                             .findFirst()
                             .map(e -> e.getText().trim()).orElse(null);
        }

        String desc = psiMethod.getContainingClass().getName().concat("#").concat(psiMethod.getName());

        if (StringUtils.isNotEmpty(restDoc)) {
            return desc + "#" + restDoc;
        }

        return desc ;
    }

    public static class JavaLanguageResolverProvider implements LanguageResolverProvider {

        @NotNull
        @Override
        public LanguageResolver createLanguageResolver() {
            return new JavaLanguageResolver();
        }
    }
}
