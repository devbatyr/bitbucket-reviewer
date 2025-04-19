package com.shakenov.bitbucket.reviewer.service;

import com.shakenov.bitbucket.reviewer.client.BitbucketApiClient;
import com.shakenov.bitbucket.reviewer.model.BitbucketCommentRequest;
import com.shakenov.bitbucket.reviewer.model.BitbucketPRContext;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class BitbucketCommentPublisherTest {

    private BitbucketCommentPublisher publisher;
    private BitbucketApiClient mockClient;
    private Response mockResponse;

    @BeforeEach
    void setup() {
        publisher = new BitbucketCommentPublisher();
        mockClient = mock(BitbucketApiClient.class);
        mockResponse = mock(Response.class);
        publisher.apiClient = mockClient;
    }

    @Test
    void testPublishSuccess() {
        BitbucketPRContext context = new BitbucketPRContext();
        context.setWorkspace("workspace");
        context.setRepoSlug("repo");
        context.setPrId(1);

        when(mockClient.postComment(anyString(), anyString(), anyInt(), any()))
                .thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);

        publisher.publish(context, "Hello PR!");

        verify(mockClient).postComment(eq("workspace"), eq("repo"), eq(1), any(BitbucketCommentRequest.class));
        verify(mockResponse).close();
    }

    @Test
    void testPublishFailure() {
        BitbucketPRContext context = new BitbucketPRContext();
        context.setWorkspace("workspace");
        context.setRepoSlug("repo");
        context.setPrId(1);

        when(mockClient.postComment(anyString(), anyString(), anyInt(), any()))
                .thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(500);
        when(mockResponse.readEntity(String.class)).thenReturn("Internal Server Error");

        publisher.publish(context, "Should log error");

        verify(mockResponse).readEntity(String.class);
        verify(mockResponse).close();
    }
}
