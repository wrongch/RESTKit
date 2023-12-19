package io.github.wrongch.restkit.config;

import com.intellij.openapi.project.Project;
import io.github.wrongch.restkit.common.BKV;
import io.github.wrongch.restkit.common.EnvList;
import io.github.wrongch.restkit.common.KV;
import io.github.wrongch.restkit.util.ScriptUtils;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Environment
 *
 * @author huzunrong
 * @since 1.0
 */
@Data
public class Environment {

    private String currentEnv;
    private List<EnvList> envList = Collections.emptyList();
    private List<BKV> globalHeaderList = Collections.emptyList();
    private String script;

    public static Environment getInstance(Project project) {
        return project.getService(EnvironmentComponent.class).getState();
    }

    public String getCurrentEnv() {
        if (CollectionUtils.isEmpty(envList)) {
            return "";
        }
        Optional<EnvList> first = envList.stream()
                                         .filter(envList -> envList.getEnv().equals(currentEnv))
                                         .findFirst();
        if (first.isPresent()) {
            return currentEnv;
        }
        currentEnv = envList.get(0).getEnv();
        return currentEnv;
    }

    public List<String> getEnvKeys() {
        return CollectionUtils.isEmpty(envList)
                ? Collections.emptyList()
                : envList.stream().map(EnvList::getEnv).distinct().collect(Collectors.toList());
    }

    public Map<String, String> getCurrentEnabledEnvMap() {
        if (CollectionUtils.isEmpty(envList)) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new LinkedHashMap<>(8);
        Optional<EnvList> first = envList.stream()
                                         .filter(envList -> envList.getEnv().equals(currentEnv))
                                         .findFirst();
        first.orElse(envList.get(0)).getItems().forEach(item -> {
            if (item.getEnabled()) {
                map.put(item.getKey(), item.getValue());
            }
        });
        return map;
    }

    public List<KV> getEnabledGlobalHeader() {
        if (CollectionUtils.isEmpty(globalHeaderList)) {
            return Collections.emptyList();
        }
        List<KV> list = new ArrayList<>();

        for (BKV bkv : globalHeaderList) {
            if (bkv.getEnabled() && StringUtils.isNotBlank(bkv.getKey())) {
                list.add(new KV(bkv.getKey(), bkv.getValue()));
            }
        }
        return list;
    }

    public Map<String, Method> getScriptMethodMap() {
        return ScriptUtils.getScriptMethodMapFromJava(script);
    }
}