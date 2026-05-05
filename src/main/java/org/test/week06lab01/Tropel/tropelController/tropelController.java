package org.test.week06lab01.Tropel.tropelController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.week06lab01.Tropel.DTO.PageResponse;
import org.test.week06lab01.Tropel.Species;
import org.test.week06lab01.Tropel.Tropel;
import org.test.week06lab01.Tropel.VitalState;
import org.test.week06lab01.Tropel.service.TropelService;

@RestController
@RequestMapping("/api/v1/tropels")
public class TropelController {

    private final TropelService tropelService;

    public TropelController(TropelService tropelService) {
        this.tropelService = tropelService;
    }

    @PostMapping
    public ResponseEntity<Tropel> crear(@RequestBody Tropel tropel) {
        Tropel tropelSaved =tropelService.save(tropel);
        return ResponseEntity.ok(tropelSaved);
    }


    @GetMapping
    public ResponseEntity<PageResponse<Tropel>> listar(
            @RequestParam(required = false) Species species,
            @RequestParam(required = false) VitalState vitalState,
            @RequestParam(required = false) Long sectorId,
            @RequestParam(required = false) Long guardianId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<Tropel> response =
                tropelService.listar(species, vitalState, sectorId, guardianId, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tropel> obtener(@PathVariable Long id) {
        Tropel tropel =tropelService.findById(id);
        if (tropel != null) {
            return ResponseEntity.ok(tropel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}