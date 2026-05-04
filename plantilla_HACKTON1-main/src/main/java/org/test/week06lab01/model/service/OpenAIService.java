package org.comunidad.git_model.service;


import org.comunidad.git_model.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    @Value("${github.token}")
    private String API_KEY;

    public String preguntarIA(String mensajeUsuario, String asunto) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "https://models.inference.ai.azure.com/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {"role": "user", 
                "content": "Genera únicamente un correo formal en español. No explicaciones. No introducciones como 'Claro' o 'Aquí tienes'. No uses separadores. Tema: %s. Contexto/destinatario: %s. Devuelve solo el correo listo para enviar."
             }
          ]
        }
        """.formatted(asunto,mensajeUsuario);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            ChatResponse chatResponse =
                    mapper.readValue(response.getBody(), ChatResponse.class);

            return chatResponse.choices.get(0).message.content.trim();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}