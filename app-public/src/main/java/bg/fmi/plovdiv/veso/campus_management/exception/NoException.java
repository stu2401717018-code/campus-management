package bg.fmi.plovdiv.veso.campus_management.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NoException extends RuntimeException
{
    private static final String NAAS_URL = "https://naas.isalman.dev/no";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public NoException() throws IOException, InterruptedException
    {
        super(fetchNoReason());
    }

    public NoException(String fallbackMessage)
    {
        super(fallbackMessage);
    }

    private static String fetchNoReason() throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(NAAS_URL))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = OBJECT_MAPPER.readTree(response.body());

            return root.path("reason").asText("No.");
        }

        return "No.";
    }
}
