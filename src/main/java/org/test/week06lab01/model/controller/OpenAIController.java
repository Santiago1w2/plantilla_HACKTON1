package org.test.week06lab01.model.controller;

import org.springframework.web.bind.annotation.*;
import org.test.week06lab01.model.service.OpenAIService;

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
