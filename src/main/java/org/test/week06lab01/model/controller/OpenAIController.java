package org.comunidad.git_model.controller;

import org.comunidad.git_model.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class OpenAIController {

    private final OpenAIService service;

    public OpenAIController(OpenAIService service) {
        this.service = service;
    }

    @PostMapping("/preguntar")
    public String preguntar(@RequestBody String pregunta, @RequestBody String asunto) {
        return service.preguntarIA(pregunta,asunto);
    }
}
