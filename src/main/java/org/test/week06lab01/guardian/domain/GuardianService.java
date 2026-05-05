package org.test.week06lab01.guardian.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.test.week06lab01.guardian.infrastructure.GuardianRepository;

import java.time.Instant;
import java.util.List;

public class GuardianService {

    private final GuardianRepository guardianRepository;

    public GuardianService(GuardianRepository guardianRepository){this.guardianRepository = guardianRepository;
    };

    // Implementación de findById
    public Guardian findById(Long id) {
        return guardianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guardian no encontrado con el ID: "));
    }

    // Para el mapeo de listar que hicimos antes
    public List<Guardian> findAll() {
        return guardianRepository.findAll();
    }

    public Guardian createGuardian(Guardian guardian) {
        // 1. Verificar si el email ya existe para lanzar el 409
        if (guardianRepository.existsByEmail(guardian.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        // 2. Asignar la fecha actual antes de guardar
        guardian.setCreatedAt(Instant.now());

        return guardianRepository.save(guardian);
    }

}
