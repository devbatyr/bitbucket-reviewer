package com.shakenov.bitbucket.reviewer.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Configuration interface for accessing Ollama-related settings.
 * <p>
 * These settings control how the AI model is invoked and what prompt template to use.
 * Properties are expected to be defined with the prefix {@code ollama} in {@code application.properties}.
 *
 * <p><b>Example:</b>
 * <pre>
 * ollama.model=deepseek-coder:6.7b
 * ollama.temperature=0.2
 * ollama.prompt-path=prompt-template.txt
 * </pre>
 */
@ConfigMapping(prefix = "ollama")
public interface OllamaProperties {

    /**
     * The name of the AI model to use in Ollama (e.g., {@code deepseek-coder:6.7b}).
     *
     * @return model name string
     */
    String model();

    /**
     * Temperature parameter for the model (controls randomness). Lower values make output more deterministic.
     *
     * @return temperature value (e.g., 0.2)
     */
    double temperature();

    /**
     * Path to the prompt template file used for generating the AI prompt.
     * Default is {@code prompt-template.txt} in the project root.
     *
     * @return file path to prompt template
     */
    @WithDefault("prompt-template.txt")
    String promptPath();
}
