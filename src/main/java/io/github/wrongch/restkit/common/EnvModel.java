package io.github.wrongch.restkit.common;

import io.github.wrongch.restkit.config.KeyValueTableModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * EnvModel
 *
 * @author huzunrong
 * @since 1.0.8
 */
@Data
@AllArgsConstructor
public class EnvModel {

    private String env;

    private KeyValueTableModel model;
}
