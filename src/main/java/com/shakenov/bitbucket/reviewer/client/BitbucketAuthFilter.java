package com.shakenov.bitbucket.reviewer.client;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import com.shakenov.bitbucket.reviewer.config.BitbucketTokenProvider;

import java.io.IOException;

/**
 * JAX-RS client request filter that automatically injects the Authorization header
 * into all outgoing HTTP requests to the Bitbucket REST API.
 * <p>
 * This filter retrieves the token from {@link BitbucketTokenProvider}
 * and appends it as a Basic Auth header.
 * </p>
 *
 * Example:
 * <pre>
 * Authorization: Basic TOKEN
 * </pre>
 *
 * This allows centralized and reusable handling of authentication
 * across all REST client requests.
 *
 * @see BitbucketTokenProvider
 */
@Provider
public class BitbucketAuthFilter implements ClientRequestFilter {

    @Inject
    BitbucketTokenProvider tokenProvider;

    /**
     * Adds the Authorization header to each outgoing request using Basic authentication.
     *
     * @param requestContext the context of the HTTP request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("Authorization", "Basic " + tokenProvider.getToken());
    }
}
