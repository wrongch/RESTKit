package io.github.wrongch.restkit.common;

import io.github.wrongch.restkit.restful.RestClient;
import lombok.Data;

import java.util.Map;

/**
 * Request
 *
 * @author huzunrong
 * @since 2.0.0
 */
@Data
public class Request {

    private String url;
    private String method;
    private Map<String, String> config;
    private Map<String, String> headers;
    private Map<String, String> params;
    private String body;

    private RestClient client;
}