package io.github.wrongch.restkit.restful;

import com.intellij.openapi.project.Project;
import io.github.wrongch.restkit.common.KV;
import io.github.wrongch.restkit.common.Request;
import io.github.wrongch.restkit.common.RequestInfo;
import io.github.wrongch.restkit.common.RestClientData;
import io.github.wrongch.restkit.common.RestItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * rest client extension point
 *
 * @author huzunrong
 * @since 2.0.3
 */
public interface RestClient {

    /**
     * supported protocol
     */
    @NotNull
    String getProtocol();

    /**
     * get config param for Config tab
     * can use Environment variable placeholder
     */
    List<KV> getConfig(@NotNull RestItem restItem, @NotNull Project project);

    /**
     * 创建请求
     *
     * @param restClientData
     * @param project
     */
    @NotNull
    Request createRequest(RestClientData restClientData, Project project);

    /**
     * 发送请求
     *
     * @param request created by #createRequest()
     */
    @NotNull
    RequestInfo sendRequest(Request request, Project project);

    /**
     * 格式化rest client中的Response
     *
     * @param requestInfo
     */
    @NotNull
    String formatResponseInfo(RequestInfo requestInfo);

    /**
     * 格式化输出的日志
     *
     * @param requestInfo
     */
    @NotNull
    String formatLogInfo(RequestInfo requestInfo);
}