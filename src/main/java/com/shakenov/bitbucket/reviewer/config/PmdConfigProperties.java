package com.shakenov.bitbucket.reviewer.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "pmd")
public interface PmdConfigProperties {

    /**
     * Absolute path to the PMD CLI executable.
     */
    @WithDefault("pmd")
    String path();

    /**
     * Absolute path to the PMD ruleset XML file.
     */
    String ruleset();

    /**
     * Optional path to a suppressions XML file.
     */
    String suppressions();
}
