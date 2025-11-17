package com.ProyectoDeAula5.Proyecto5.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class GeminiApiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final int MAX_RETRIES = 3;

    public String generarTexto(String prompt) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", prompt)))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        int attempt = 0;

        while (attempt < MAX_RETRIES) {
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        request,
                        Map.class);

                Map responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("candidates")) {
                    List candidates = (List) responseBody.get("candidates");
                    if (!candidates.isEmpty()) {
                        Map firstCandidate = (Map) candidates.get(0);
                        Map content = (Map) firstCandidate.get("content");

                        if (content != null && content.containsKey("parts")) {
                            List parts = (List) content.get("parts");
                            if (!parts.isEmpty()) {
                                Map firstPart = (Map) parts.get(0);
                                return firstPart.get("text").toString();
                            }
                        }
                    }
                }

                return "No se pudo obtener respuesta del modelo.";

            } catch (RestClientException e) {

                if (e.getMessage().contains("429")) {
                    attempt++;
                    long waitTime = 3000L * attempt;
                    System.err.println("⚠️ Error 429: Reintentando en " + waitTime + " ms...");

                    try {
                        TimeUnit.MILLISECONDS.sleep(waitTime);
                    } catch (InterruptedException ignored) {
                    }

                    continue;
                }

                return "Error en la solicitud: " + e.getMessage();
            } catch (Exception e) {
                return "Error inesperado: " + e.getMessage();
            }
        }

        return "Error: demasiados intentos fallidos por saturación (429).";
    }

    public Map<String, Object> listarModelos() {
        try {
            String url = "https://generativelanguage.googleapis.com/v1/models?key=" + apiKey;

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            return response.getBody();

        } catch (Exception e) {
            return Map.of("error", "Error al listar modelos: " + e.getMessage());
        }
    }
}