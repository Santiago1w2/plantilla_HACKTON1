package org.test.week06lab01.sector.application;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.week06lab01.sector.domain.Sector;
import org.test.week06lab01.sector.domain.SectorService;
;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sectors")
public class SectorController {

    private final SectorService sectorService;

    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    // POST /api/v1/sectors [Crear sector]
    @PostMapping
    public ResponseEntity<Sector> createSector(@Valid @RequestBody Sector sector) {
        Sector newSector = sectorService.createSector(sector);
        return new ResponseEntity<>(newSector, HttpStatus.CREATED);
    }

    // GET /api/v1/sectors [Listar sectores]
    @GetMapping
    public ResponseEntity<List<Sector>> getAllSectors() {
        List<Sector> sectors = sectorService.findAll();
        return ResponseEntity.ok(sectors);
    }

    // GET /api/v1/sectors/{id} [Ver detalle de sector]
    @GetMapping("/{id}")
    public ResponseEntity<Sector> getSectorById(@PathVariable Long id) {
        Sector sector = sectorService.findById(id);
        return ResponseEntity.ok(sector);
    }
}
