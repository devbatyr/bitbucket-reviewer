package com.shakenov.bitbucket.reviewer.service;

import com.shakenov.bitbucket.reviewer.client.OllamaClient;
import com.shakenov.bitbucket.reviewer.config.OllamaProperties;
import com.shakenov.bitbucket.reviewer.model.OllamaRequest;
import com.shakenov.bitbucket.reviewer.model.OllamaResponse;
import com.shakenov.pmdcore.model.PmdViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AiRecommendationServiceTest {

    private OllamaClient ollamaClient;
    private OllamaProperties ollamaProperties;
    private AiRecommendationService aiRecommendationService;

    @BeforeEach
    public void setUp() {
        ollamaClient = Mockito.mock(OllamaClient.class);
        ollamaProperties = Mockito.mock(OllamaProperties.class);

        aiRecommendationService = new AiRecommendationService();
        aiRecommendationService.ollamaClient = ollamaClient;
        aiRecommendationService.ollamaProperties = ollamaProperties;

        when(ollamaProperties.model()).thenReturn("deepseek-coder:6.7b");
        when(ollamaProperties.temperature()).thenReturn(0.2);
        when(ollamaProperties.promptPath()).thenReturn("nonexistent-path.txt");
    }

    @Test
    public void testGetRecommendationReturnsResponse() {
        String codeSnippet = "for (int i = 0; i < 10; i++) new Object();";
        PmdViolation violation = new PmdViolation("AvoidInstantiatingObjectsInLoops", "Avoid instantiating new objects inside loops", 42, "");

        when(ollamaClient.analyze(any(OllamaRequest.class)))
                .thenReturn(new OllamaResponse("Mocked AI suggestion"));

        String result = aiRecommendationService.getRecommendation(List.of(violation), codeSnippet);

        assertEquals("Mocked AI suggestion", result);
        verify(ollamaClient, times(1)).analyze(any(OllamaRequest.class));
    }

    @Test
    public void testFallbackPromptUsedIfFileMissing() {
        PmdViolation violation = new PmdViolation("SystemPrintln", "Avoid using System.out.println", 13, "");
        String codeSnippet = "System.out.println(\"Hello\");";

        when(ollamaClient.analyze(any(OllamaRequest.class)))
                .thenReturn(new OllamaResponse("Fallback prompt used successfully"));

        String result = aiRecommendationService.getRecommendation(List.of(violation), codeSnippet);

        assertEquals("Fallback prompt used successfully", result);
    }
}
