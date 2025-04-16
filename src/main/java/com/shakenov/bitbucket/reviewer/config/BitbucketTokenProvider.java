package com.shakenov.bitbucket.reviewer.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides the Bitbucket authentication token from configuration.
 * <p>
 * The token is expected to be a Base64-encoded value of the format {@code username:password}
 * and is injected from the configuration property {@code bitbucket.auth.token}.
 * This token is typically used for Basic authentication in REST API requests.
 */
@ApplicationScoped
public class BitbucketTokenProvider {

    @ConfigProperty(name = "bitbucket.auth.token")
    String token;

    /**
     * Returns the raw Base64-encoded Bitbucket Basic Auth token.
     *
     * @return the authentication token from configuration
     */
    public String getToken() {
        return token;
    }
}
