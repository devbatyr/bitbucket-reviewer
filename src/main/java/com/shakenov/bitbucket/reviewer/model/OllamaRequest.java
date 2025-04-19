package com.shakenov.bitbucket.reviewer.model;

/**
 * Represents a request sent to the Ollama API for code analysis.
 *
 * @param model       The name of the AI model to use (e.g. "deepseek-coder:6.7b").
 * @param prompt      The input prompt containing code and rule violations for analysis.
 * @param temperature The sampling temperature for the model. Lower values make output more deterministic.
 * @param stream      Whether to enable streaming response (typically false for API use).
 */
public record OllamaRequest(String model, String prompt, double temperature, boolean stream) {}
