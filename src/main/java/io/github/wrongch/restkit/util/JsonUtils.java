package io.github.wrongch.restkit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author zunrong
 */
@UtilityClass
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String format(String str) {
        if (!isValidJson(str)) {
            return str;
        }
        JsonElement parse = JsonParser.parseString(str);
        return GSON.toJson(parse);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> List<T> fromJsonArr(String json, Class<T> classOfT) throws JsonSyntaxException {
        return GSON.fromJson(json, TypeToken.getParameterized(List.class, classOfT).getType());
    }

    private static boolean isValidJson(String targetStr) {
        if (StringUtils.isEmpty(targetStr)) {
            return false;
        }
        return isJson(targetStr, JsonObject.class) || isJson(targetStr, JsonArray.class);
    }

    private static boolean isJson(String targetStr, Class<?> clazz) {
        try {
            GSON.fromJson(targetStr, clazz);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}