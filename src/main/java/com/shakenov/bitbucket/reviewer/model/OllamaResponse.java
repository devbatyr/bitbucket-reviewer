package com.shakenov.bitbucket.reviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a response from the Ollama API.
 * <p>
 * The {@code response} field contains the generated recommendation text based on the prompt.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OllamaResponse(String response) {}
