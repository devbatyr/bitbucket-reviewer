package com.shakenov.bitbucket.reviewer.service;

import com.shakenov.bitbucket.reviewer.client.BitbucketApiClient;
import com.shakenov.bitbucket.reviewer.config.PmdConfigProperties;
import com.shakenov.bitbucket.reviewer.model.BitbucketPRContext;
import com.shakenov.pmdcore.config.PmdConfig;
import com.shakenov.pmdcore.model.PmdResponse;
import com.shakenov.pmdcore.model.PmdViolation;
import com.shakenov.pmdcore.service.PmdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BitbucketServiceTest {

    private BitbucketService service;
    private BitbucketApiClient apiClient;
    private CommitService commitService;
    private DiffParserService diffParser;
    private AiRecommendationService aiRecommendationService;
    private BitbucketCommentPublisher commentPublisher;
    private PmdConfigProperties pmdProps;
    private PmdService pmdService;

    @BeforeEach
    void setUp() throws Exception {
        service = new BitbucketService();
        apiClient = mock(BitbucketApiClient.class);
        commitService = mock(CommitService.class);
        diffParser = mock(DiffParserService.class);
        aiRecommendationService = mock(AiRecommendationService.class);
        commentPublisher = mock(BitbucketCommentPublisher.class);
        pmdProps = mock(PmdConfigProperties.class);
        pmdService = mock(PmdService.class);

        Field field = BitbucketService.class.getDeclaredField("pmdService");
        field.setAccessible(true);
        field.set(service, pmdService);

        service.apiClient = apiClient;
        service.commitService = commitService;
        service.diffParser = diffParser;
        service.aiRecommendationService = aiRecommendationService;
        service.commentPublisher = commentPublisher;
        service.pmdProperties = pmdProps;
    }

    @Test
    void testAnalyzePullRequest_withViolations_shouldPublishComment() {
        BitbucketPRContext context = new BitbucketPRContext();
        context.setWorkspace("workspace");
        context.setRepoSlug("repo");
        context.setPrId(1);

        when(apiClient.getPullRequestDetails(any(), any(), anyInt())).thenReturn("{commit:1234}");
        when(commitService.extractCommitSha(any())).thenReturn("1234");
        when(apiClient.getPullRequestDiff(any(), any(), anyInt())).thenReturn("diff content");
        when(diffParser.extractModifiedPaths(any())).thenReturn(List.of("src/File.java"));
        when(apiClient.getFileContent(any(), any(), any(), any())).thenReturn("public class Test {}");

        when(pmdProps.path()).thenReturn("/pmd");
        when(pmdProps.ruleset()).thenReturn("/ruleset.xml");
        when(pmdProps.suppressions()).thenReturn("/supp.xml");

        PmdViolation violation = new PmdViolation("Rule1", "desc", 2, "");
        PmdResponse response = new PmdResponse("raw", "formatted", List.of(violation));

        when(pmdService.runPmd(any(PmdConfig.class))).thenReturn(response);
        when(aiRecommendationService.getRecommendation(any(), any())).thenReturn("AI Suggestion");

        service.analyzePullRequest(context);

        verify(commentPublisher).publish(eq(context), contains("AI Suggestion"));
    }
}
