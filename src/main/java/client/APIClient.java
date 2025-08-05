package client;

import exceptions.ApiException;
import exceptions.TypeSafetyException;
import utils.JsonService;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class APIClient {

    private final HttpClient httpClient;
    private final JsonService jsonService;
    private final String baseUrl;

    public APIClient(String baseUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.jsonService = new JsonService();
        this.baseUrl = baseUrl;
    }

    public <REQ, RESP> APIResponse<RESP> post(String endpoint, REQ request, Class<RESP> responseType)
            throws ApiException {
        try{
            String requestBody = jsonService.serializeRequest(request);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            return handleResponse(httpResponse, responseType);

        } catch (TypeSafetyException e) {
            throw new ApiException("Type safety error: " + e.getMessage(), e);
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP request failed: " + e.getMessage(), e);
        }
    }

    public <RESP> APIResponse<RESP> get(String endpoint, Class<RESP> responseType)
            throws ApiException {

        try {
            // Build HTTP request
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            // Execute request
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            return handleResponse(httpResponse, responseType);

        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP request failed: " + e.getMessage(), e);
        }
    }

    public APIResponse<Void> delete(String endpoint) throws ApiException {
        try {
            // Build HTTP request
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .DELETE()
                    .build();

            // Execute request
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            // Return simple response for DELETE
            return new APIResponse<>(
                    httpResponse.statusCode(),
                    null,
                    httpResponse.headers().map()
            );

        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP request failed: " + e.getMessage(), e);
        }
    }

    private <RESP> APIResponse<RESP> handleResponse(HttpResponse<String> httpResponse,
                                                    Class<RESP> responseType) throws ApiException {

        String responseBody = httpResponse.body();
        int statusCode = httpResponse.statusCode();
        Map<String, List<String>> headers = httpResponse.headers().map();

        // Handle successful responses (2xx)
        if (statusCode >= 200 && statusCode < 300) {
            try {
                RESP responseObject = null;
                if (responseBody != null && !responseBody.trim().isEmpty() && responseType != Void.class) {
                    responseObject = jsonService.deserializeResponse(responseBody, responseType);
                }
                return new APIResponse<>(statusCode, responseObject, headers);
            } catch (TypeSafetyException e) {
                throw new ApiException(
                        String.format("Failed to deserialize response (Status: %d): %s",
                                statusCode, e.getMessage()), e);
            }
        }
        else {
            throw new ApiException(
                    String.format("API request failed with status %d: %s",
                            statusCode, responseBody));
        }
    }
}
