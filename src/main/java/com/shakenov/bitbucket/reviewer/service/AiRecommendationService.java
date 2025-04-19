package com.shakenov.bitbucket.reviewer.service;

import com.shakenov.bitbucket.reviewer.client.OllamaClient;
import com.shakenov.bitbucket.reviewer.config.OllamaProperties;
import com.shakenov.bitbucket.reviewer.model.OllamaRequest;
import com.shakenov.bitbucket.reviewer.model.OllamaResponse;
import com.shakenov.pmdcore.model.PmdViolation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Service responsible for generating AI-based code review recommendations
 * by sending PMD violations and code snippets to an Ollama API model.
 * <p>
 * This service constructs a prompt from rule violations and code,
 * sends it to the AI model, and returns the generated recommendations.
 */
@ApplicationScoped
public class AiRecommendationService {

    private static final Logger log = Logger.getLogger(AiRecommendationService.class);

    @Inject @RestClient
    OllamaClient ollamaClient;

    @Inject
    OllamaProperties ollamaProperties;

    /**
     * Sends a list of PMD violations and code to the Ollama model
     * and returns AI-generated recommendations.
     *
     * @param violations  List of rule violations detected by PMD.
     * @param codeSnippet The corresponding Java code with violations.
     * @return AI-generated review recommendation string.
     */
    public String getRecommendation(List<PmdViolation> violations, String codeSnippet) {
        try {
            String prompt = buildPrompt(violations, codeSnippet);

            OllamaRequest request = new OllamaRequest(
                    ollamaProperties.model(),
                    prompt,
                    ollamaProperties.temperature(),
                    false
            );

            OllamaResponse response = ollamaClient.analyze(request);
            return response.response();
        } catch (ProcessingException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SocketTimeoutException || cause instanceof TimeoutException) {
                log.errorf("Ollama API timeout: %s", cause.getMessage());
            } else {
                log.error("Ollama API error", e);
            }
            return "⚠️ Could not retrieve AI recommendation due to timeout.";
        }
    }

    /**
     * Builds a prompt string from a list of violations and code snippet,
     * inserting them into a customizable template.
     *
     * @param violations  List of PMD violations.
     * @param codeSnippet Raw Java code to analyze.
     * @return Final prompt string ready to send to Ollama API.
     */
    private String buildPrompt(List<PmdViolation> violations, String codeSnippet) {
        String template = loadTemplate();

        Set<String> seen = new HashSet<>();
        String violationsBlock = violations.stream()
                .filter(v -> seen.add(v.getRule() + "|" + v.getDescription()))
                .map(this::formatViolation)
                .collect(Collectors.joining("\n"));

        return template
                .replace("{{violations}}", violationsBlock)
                .replace("{{code}}", codeSnippet);
    }

    /**
     * Formats a single violation into a readable line for the AI prompt.
     *
     * @param v The violation to format.
     * @return A formatted string like "- Rule: X | Message: Y | Line: Z".
     */
    private String formatViolation(PmdViolation v) {
        return "- Rule: %s | Message: %s | Line: %d"
                .formatted(v.getRule(), v.getDescription(), v.getLine());
    }

    /**
     * Loads the prompt template from file. If the file is not found,
     * falls back to a default template.
     *
     * @return The loaded or fallback prompt template.
     */
    private String loadTemplate() {
        try {
            return Files.readString(Paths.get(ollamaProperties.promptPath()));
        } catch (IOException e) {
            log.warn("Prompt file not found, using fallback prompt.");
            return """
            For each violation below, provide a one-sentence fix recommendation.

            Violations:
            {{violations}}

            Code:
            {{code}}
            """;
        }
    }
}
