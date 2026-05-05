package org.test.week06lab01.sector.domain;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.test.week06lab01.sector.infrastructure.SectorRepository;

import java.time.Instant;
import java.util.List;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    // Listar todos los sectores
    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    // Obtener detalle de un sector por ID
    public Sector findById(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sector no encontrado"));
    }

    // Crear un nuevo sector
    public Sector createSector(Sector sector) {
        // Validación de negocio: La carga actual no puede superar la capacidad
        if (sector.getCurrentLoad() > sector.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carga actual supera la capacidad máxima");
        }

        // Asignar fecha de creación
        sector.setCreatedAt(Instant.now());

        return sectorRepository.save(sector);
    }

    // Método para actualizar la carga de un sector (muy común para esta entidad)
    public Sector updateLoad(Long id, int newLoad) {
        Sector sector = findById(id);

        if (newLoad > sector.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva carga supera la capacidad");
        }

        sector.setCurrentLoad(newLoad);
        return sectorRepository.save(sector);
    }
}