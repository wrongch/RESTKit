package io.github.newhoo.restkit.common;

import com.intellij.openapi.actionSystem.DataKey;

import java.util.List;

/**
 * data keys
 */
public class RestDataKey {

    public static final DataKey<List<RestItem>> ALL_SERVICE = DataKey.create("ALL_SERVICE");

    public static final DataKey<List<RestItem>> SELECTED_SERVICE = DataKey.create("SELECTED_SERVICE");

    public static final DataKey<List<RestItem>> SELECTED_MODULE = DataKey.create("SELECTED_MODULE");

    public static final DataKey<RestClientEditorInfo> CLIENT_EDITOR_INFO = DataKey.create("CLIENT_EDITOR_INFO");

    public static final DataKey<RestClientApiInfo> CLIENT_API_INFO = DataKey.create("CLIENT_API_INFO");
}