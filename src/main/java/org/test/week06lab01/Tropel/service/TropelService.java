package org.test.week06lab01.Tropel.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.week06lab01.Tropel.DTO.PageResponse;
import org.test.week06lab01.Tropel.Species;
import org.test.week06lab01.Tropel.Tropel;
import org.test.week06lab01.Tropel.VitalState;
import org.test.week06lab01.Tropel.tropelRepository.TropelRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TropelService {

    private final TropelRepository tropelRepository;

    public TropelService(TropelRepository tropelRepository){
        this.tropelRepository = tropelRepository;
    }

    public Tropel save(Tropel tropel){
        if (tropel != null) {
            return tropelRepository.save(tropel);
        } else {
            throw new IllegalArgumentException("Tropel vacio");
        }
    }

    public PageResponse<Tropel> listar(Species species,
                                       VitalState vitalState,
                                       Long sectorId,
                                       Long guardianId,
                                       int page,
                                       int size) {

        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        size = Math.min(size, 50);

        Specification<Tropel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (species != null) {
                predicates.add(cb.equal(root.get("species"), species));
            }

            if (vitalState != null) {
                predicates.add(cb.equal(root.get("vitalState"), vitalState));
            }

            if (sectorId != null) {
                predicates.add(cb.equal(root.get("sector").get("id"), sectorId));
            }

            if (guardianId != null) {
                predicates.add(cb.equal(root.get("guardian").get("id"), guardianId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Tropel> result = tropelRepository.findAll(
                spec,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        return new PageResponse<>(
                result.getContent(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }


    public Tropel findById(Long id) {
        return tropelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tropel no encontrado"));
    }
}