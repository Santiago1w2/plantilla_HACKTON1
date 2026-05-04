package org.test.week06lab01.Tropel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.week06lab01.Tropel.Tropel;

@Service
public class TropelService {

    private final TropelRepository tropelRepository;
    private final SectorRepository sectorRepository;
    private final GuardianRepository guardianRepository;

    public TropelService(TropelRepository tropelRepository,
                         SectorRepository sectorRepository,
                         GuardianRepository guardianRepository) {
        this.tropelRepository = tropelRepository;
        this.sectorRepository = sectorRepository;
        this.guardianRepository = guardianRepository;
    }

    @Transactional
    public Tropel crearTropel(CreateTropelRequest request) {

        // 1. Buscar Sector
        Sector sector = sectorRepository.findById(request.getSectorId())
                .orElseThrow(() -> new RuntimeException("Sector no encontrado"));

        // 2. Buscar Guardian
        Guardian guardian = guardianRepository.findById(request.getGuardianId())
                .orElseThrow(() -> new RuntimeException("Guardian no encontrado"));

        // 3. Validar capacidad
        if (sector.getCurrentLoad() >= sector.getCapacity()) {
            throw new RuntimeException("Sector lleno");
        }

        // 4. Crear Tropel (valores iniciales ya están en la entidad)
        Tropel tropel = new Tropel();
        tropel.setName(request.getName());
        tropel.setSpecies(request.getSpecies());
        tropel.setSector(sector);
        tropel.setGuardian(guardian);

        // (vitalState, energyLevel, etc ya tienen valores por defecto)

        // 5. Incrementar carga del sector
        sector.setCurrentLoad(sector.getCurrentLoad() + 1);
        sectorRepository.save(sector);

        // 6. Guardar Tropel
        return tropelRepository.save(tropel);
    }
}
