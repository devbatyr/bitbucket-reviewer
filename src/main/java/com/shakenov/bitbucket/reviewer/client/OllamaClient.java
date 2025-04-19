package com.shakenov.bitbucket.reviewer.client;

import com.shakenov.bitbucket.reviewer.model.OllamaRequest;
import com.shakenov.bitbucket.reviewer.model.OllamaResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client interface for interacting with the Ollama API.
 * <p>
 * This client is registered with the {@code ollama-api} config key and is responsible for sending
 * code analysis prompts to the Ollama model and receiving generated recommendations.
 *
 * <p><b>Example config:</b>
 * <pre>
 * quarkus.rest-client.ollama-api.url=http://localhost:11435
 * quarkus.rest-client.ollama-api.connect-timeout=3000
 * quarkus.rest-client.ollama-api.read-timeout=30000
 * </pre>
 */
@RegisterRestClient(configKey = "ollama-api")
@Path("/api/generate")
public interface OllamaClient {

    /**
     * Sends a request to the Ollama API for code analysis and recommendation.
     *
     * @param request the Ollama request containing the model name, prompt, and settings
     * @return response from the Ollama model with generated text
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    OllamaResponse analyze(OllamaRequest request);
}
