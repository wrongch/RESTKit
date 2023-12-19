package io.github.wrongch.restkit.toolwindow;

import com.intellij.util.messages.Topic;
import io.github.wrongch.restkit.common.RestItem;

/**
 * RestServiceListener
 *
 * @author huzunrong
 * @since 1.0.8
 */
public interface RestServiceListener {

    Topic<RestServiceListener> REST_SERVICE_SELECT = Topic.create("RestServiceSelect", RestServiceListener.class);

    void select(RestItem serviceItem);
}
