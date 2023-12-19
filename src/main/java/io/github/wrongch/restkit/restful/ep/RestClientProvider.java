package io.github.wrongch.restkit.restful.ep;

import com.intellij.openapi.extensions.ExtensionPointName;
import io.github.wrongch.restkit.restful.RestClient;

/**
 * RestClientProvider
 *
 * @author huzunrong
 * @since 2.0.3
 */
public interface RestClientProvider {

    ExtensionPointName<RestClientProvider> EP_NAME = ExtensionPointName.create("io.github.wrongch.restkit.restClient");

    RestClient createClient();
}
