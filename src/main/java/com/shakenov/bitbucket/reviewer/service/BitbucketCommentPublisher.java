package com.shakenov.bitbucket.reviewer.service;

import com.shakenov.bitbucket.reviewer.client.BitbucketApiClient;
import com.shakenov.bitbucket.reviewer.model.BitbucketCommentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import com.shakenov.bitbucket.reviewer.model.BitbucketPRContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

/**
 * Responsible for publishing comments to a Bitbucket pull request.
 * This service uses the Bitbucket REST API to post general comments.
 */
@ApplicationScoped
public class BitbucketCommentPublisher {

    private static final Logger log = Logger.getLogger(BitbucketCommentPublisher.class);

    @Inject
    @RestClient
    BitbucketApiClient apiClient;

    /**
     * Publishes a general comment to the given pull request.
     *
     * @param context the pull request context (workspace, repo, PR ID, token)
     * @param message the comment text to post
     */
    public void publish(BitbucketPRContext context, String message) {
        Response response = apiClient.postComment(
                context.getWorkspace(),
                context.getRepoSlug(),
                context.getPrId(),
                new BitbucketCommentRequest(message)
        );

        if (response.getStatus() >= 300) {
            String body = response.readEntity(String.class);
            log.error("Error publishing comment: " + response.getStatus() + " " + body);
        }

        response.close();
    }
}
