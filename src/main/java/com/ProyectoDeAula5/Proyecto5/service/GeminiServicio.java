package com.ProyectoDeAula5.Proyecto5.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service("geminiServicio")
public class GeminiServicio {

    @Value("${gemini.api.key}")
    private String claveApi;

    @Value("${gemini.api.url}")
    private String urlApi;

    // Configurables
    @Value("${gemini.reintentos.max:3}")
    private int maxReintentos;

    @Value("${gemini.reintentos.backoffInicialMs:800}")
    private long backoffInicial;

    @Value("${gemini.respuesta.maxCaracteres:0}") // 0 = sin límite
    private int maxCaracteresRespuesta;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generarTexto(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("role", "user",
                                "parts", List.of(Map.of("text", prompt)))));
        return enviarConReintentos(body);
    }

    public String generarTextoConHistorial(List<Map<String, Object>> contenidos) {
        Map<String, Object> body = Map.of("contents", contenidos);
        return enviarConReintentos(body);
    }

    private String enviarConReintentos(Map<String, Object> body) {
        int intento = 0;
        long espera = backoffInicial;

        while (true) {
            try {
                HttpHeaders cabeceras = new HttpHeaders();
                cabeceras.setContentType(MediaType.APPLICATION_JSON);
                cabeceras.set("x-goog-api-key", claveApi);

                HttpEntity<Map<String, Object>> entidad = new HttpEntity<>(body, cabeceras);

                ResponseEntity<Map> respuesta = restTemplate.exchange(
                        urlApi,
                        HttpMethod.POST,
                        entidad,
                        Map.class);

                int status = respuesta.getStatusCode().value();
                if (status == 429) {
                    // Limite: aplicar backoff y reintentar
                    if (intento >= maxReintentos) {
                        return "Se alcanzó el límite de reintentos por saturación (429). Intenta más tarde.";
                    }
                    dormir(espera);
                    espera *= 2; // backoff exponencial
                    intento++;
                    continue;
                } else if (status >= 500 && status < 600) {
                    // Opcional: tratar 5xx igual que 429
                    if (intento >= maxReintentos) {
                        return "Error temporal del servidor tras varios reintentos (" + status + ").";
                    }
                    dormir(espera);
                    espera *= 2;
                    intento++;
                    continue;
                }

                String texto = parsearTexto(respuesta.getBody());
                if (maxCaracteresRespuesta > 0 && texto.length() > maxCaracteresRespuesta) {
                    texto = texto.substring(0, maxCaracteresRespuesta) + "... [cortado]";
                }
                return texto;

            } catch (RestClientException e) {
                // Si es 429 en excepción (poco común con exchange que devuelve ResponseEntity)
                if (e.getMessage() != null && e.getMessage().contains("429")) {
                    if (intento >= maxReintentos) {
                        return "Saturación 429 tras reintentos: " + e.getMessage();
                    }
                    dormir(espera);
                    espera *= 2;
                    intento++;
                    continue;
                }
                return "Error en la solicitud: " + e.getMessage();
            } catch (Exception e) {
                return "Error inesperado: " + e.getMessage();
            }
        }
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @SuppressWarnings("unchecked")
    private String parsearTexto(Map cuerpo) {
        if (cuerpo == null)
            return "Sin respuesta del modelo.";
        Object candidatosObj = cuerpo.get("candidates");
        if (candidatosObj instanceof List candidatos && !candidatos.isEmpty()) {
            Object primero = candidatos.get(0);
            if (primero instanceof Map cand) {
                Object contenidoObj = cand.get("content");
                if (contenidoObj instanceof Map contenido) {
                    Object partesObj = contenido.get("parts");
                    if (partesObj instanceof List partes && !partes.isEmpty()) {
                        Object parte0 = partes.get(0);
                        if (parte0 instanceof Map parteMap && parteMap.get("text") != null) {
                            return parteMap.get("text").toString();
                        }
                    }
                }
                Object razon = cand.get("finishReason");
                if (razon != null) {
                    return "Respuesta detenida por la razón: " + razon;
                }
            }
        }
        return "No se pudo obtener texto de la respuesta.";
    }
}