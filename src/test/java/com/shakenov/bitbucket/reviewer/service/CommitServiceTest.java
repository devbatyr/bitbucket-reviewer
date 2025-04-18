package com.shakenov.bitbucket.reviewer.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommitServiceTest {

    private final CommitService commitService = new CommitService();

    @Test
    void shouldExtractCommitShaFromJson() {
        String json = """
                {
                  "fromRef": {
                    "latestCommit": "abc123def456"
                  }
                }
                """;

        String sha = commitService.extractCommitSha(json);
        assertEquals("abc123def456", sha);
    }

    @Test
    void shouldThrowExceptionIfCommitMissing() {
        String invalidJson = "{}";

        Exception exception = assertThrows(RuntimeException.class, () ->
                commitService.extractCommitSha(invalidJson)
        );

        assertTrue(exception.getMessage().contains("Unable to extract commit SHA from PR"));
    }
}
