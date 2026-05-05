package org.test.week06lab01.sector.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.week06lab01.sector.domain.Sector;

import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    /**
     * Verifica si existe un sector con el código proporcionado.
     * Se utiliza en el Service para validar la unicidad (Error 409).
     */
    boolean existsBySectorCode(String sectorCode);

    /**
     * Permite buscar un sector específico por su código identificador.
     */
    Optional<Sector> findBySectorCode(String sectorCode);
}