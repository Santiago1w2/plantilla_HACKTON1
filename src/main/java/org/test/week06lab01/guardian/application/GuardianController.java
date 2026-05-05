package org.test.week06lab01.guardian.application;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.week06lab01.guardian.domain.Guardian;
import org.test.week06lab01.guardian.domain.GuardianService;

import java.util.List;

@RestController
@RequestMapping("/guardian")
public class GuardianController {

    private final GuardianService guardianService;
    public GuardianController(GuardianService guardianService){this.guardianService = guardianService;}

    @GetMapping
    public ResponseEntity<List<Guardian>> getAllGuardians() {
        List<Guardian> guardians = guardianService.findAll(); // Ajusta según el método de tu service
        return ResponseEntity.ok(guardians);
    }

    // GET /api/v1/guardians/{id} [Ver detalle de un guardián]
    @GetMapping("/{id}")
    public ResponseEntity<Guardian> getGuardianById(@PathVariable Long id) {
        // Se asume que el service devuelve el objeto o lanza una excepción si no existe
        return ResponseEntity.ok(guardianService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Guardian> create(@Valid @RequestBody Guardian guardian) {
        Guardian saved = guardianService.createGuardian(guardian);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


}
