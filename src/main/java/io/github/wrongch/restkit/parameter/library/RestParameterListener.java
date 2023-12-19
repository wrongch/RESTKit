package io.github.wrongch.restkit.parameter.library;

import com.intellij.util.messages.Topic;

/**
 * RestParameterListener
 *
 * @author huzunrong
 * @since 1.0.8
 */
public interface RestParameterListener {

    Topic<RestParameterListener> REST_PARAMETER_UPDATE = Topic.create("RestParameterUpdate", RestParameterListener.class);

    void update(String editor, String content);
}
