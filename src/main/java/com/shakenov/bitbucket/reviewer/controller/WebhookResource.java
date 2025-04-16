package com.shakenov.bitbucket.reviewer.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.shakenov.bitbucket.reviewer.model.BitbucketPRContext;
import com.shakenov.bitbucket.reviewer.service.BitbucketService;
import org.jboss.logging.Logger;

/**
 * REST controller that handles incoming webhook requests from Bitbucket.
 * <p>
 * This endpoint expects a JSON payload containing pull request context
 * information and triggers static code analysis using PMD.
 */
@Path("/api/webhook")
public class WebhookResource {

    private static final Logger log = Logger.getLogger(WebhookResource.class);

    @Inject
    BitbucketService bitbucketService;

    /**
     * Endpoint for processing webhook events from Bitbucket.
     * <p>
     * This method is triggered when a POST request with PR context arrives,
     * and initiates code review analysis via {@link BitbucketService}.
     *
     * @param context the pull request context extracted from webhook payload
     * @return 200 OK if processed successfully, 500 Internal Server Error otherwise
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleWebhook(BitbucketPRContext context) {
        log.infof("Received webhook: project=%s, repo=%s, prId=%d",
                context.getWorkspace(), context.getRepoSlug(), context.getPrId());

        bitbucketService.analyzePullRequest(context);
        return Response.ok().build();
    }
}
