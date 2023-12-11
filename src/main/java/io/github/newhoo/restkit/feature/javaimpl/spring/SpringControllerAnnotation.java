package io.github.newhoo.restkit.feature.javaimpl.spring;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpringControllerAnnotation {

    CONTROLLER("Controller", "org.springframework.stereotype.Controller"),
    REST_CONTROLLER("RestController", "org.springframework.web.bind.annotation.RestController"),
    FEIGN_CLIENT("FeignClient", "org.springframework.cloud.openfeign.FeignClient");

    private final String shortName;
    private final String qualifiedName;
}