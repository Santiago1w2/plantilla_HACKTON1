package org.test.week06lab01.tropelSignal.service;
import org.test.week06lab01.CareResponse.CareResponse;
import org.test.week06lab01.CareResponse.repository.careResponseRepository;
import org.test.week06lab01.model.service.OpenAIService;
import org.test.week06lab01.exeception.BadRequestException;
import org.test.week06lab01.exeception.ResourceNotFoundException;
import org.test.week06lab01.guardian.domain.Guardian;
import org.test.week06lab01.guardian.infrastructure.GuardianRepository;
import org.test.week06lab01.sector.domain.Sector;
import org.test.week06lab01.sector.infrastructure.SectorRepository;
import org.test.week06lab01.Tropel.Tropel;
import org.test.week06lab01.Tropel.tropelRepository.TropelRepository;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;
import org.test.week06lab01.tropelSignal.dto.TropelSignalRequestDTO;
import org.test.week06lab01.tropelSignal.dto.TropelSignalResponseDTO;
import org.test.week06lab01.tropelSignal.events.TropelSignalCreatedEvent;
import org.test.week06lab01.tropelSignal.infrastructure.TropelSignalRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

@Service
@Transactional
public class TropelSignalService {

    private static final Map<String, String> SIGNAL_TO_RESPONSE_CODE = Map.of(
            "HAMBRE", "DISPATCH_NUTRIENT_PACK",
            "ABANDONO", "SEND_COMPANIONSHIP_PROTOCOL",
            "MUTACION", "ISOLATE_AND_OBSERVE",
            "FUGA", "ACTIVATE_SECTOR_LOCK",
            "CONFLICTO", "DEPLOY_MEDIATION_FIELD",
            "REPRODUCCION_MASIVA", "ENABLE_POPULATION_CONTROL",
            "SENAL_CORRUPTA", "ARCHIVE_AND_IGNORE"
    );

    private final TropelSignalRepository signalRepository;
    private final TropelRepository tropelRepository;
    private final GuardianRepository guardianRepository;
    private final SectorRepository sectorRepository;
    private final careResponseRepository careResponseRepository;
    private final OpenAIService openAIService;
    private final ApplicationEventPublisher eventPublisher;

    public TropelSignalService(TropelSignalRepository signalRepository,
                               TropelRepository tropelRepository,
                               GuardianRepository guardianRepository,
                               SectorRepository sectorRepository,
                               careResponseRepository careResponseRepository,
                               OpenAIService openAIService,
                               ApplicationEventPublisher eventPublisher) {
        this.signalRepository = signalRepository;
        this.tropelRepository = tropelRepository;
        this.guardianRepository = guardianRepository;
        this.sectorRepository = sectorRepository;
        this.careResponseRepository = careResponseRepository;
        this.openAIService = openAIService;
        this.eventPublisher = eventPublisher;
    }

    public TropelSignalResponseDTO create(TropelSignalRequestDTO req) {
        // 1. Validar existencia
        Tropel tropel = tropelRepository.findById(req.getTropelId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un Tropel con id " + req.getTropelId()));
        Guardian guardian = guardianRepository.findById(req.getGuardianId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un guardián con id " + req.getGuardianId()));

        // 2. Validar coincidencia de guardián
        if (!tropel.getGuardian().getId().equals(guardian.getId())) {
            throw new BadRequestException("El guardianId no corresponde al guardián responsable de este Tropel");
        }

        // 3. Llamar a la IA
        Map<String, String> aiResult = openAIService.preguntarIA(req.getRawContent());
        boolean isFallback = "SENAL_CORRUPTA".equals(aiResult.get("signalType")) &&
                "LEVE".equals(aiResult.get("severity")) &&
                "Archivo de Senales".equals(aiResult.get("assignedUnit")) &&
                aiResult.get("recommendedAction").startsWith("Archivar");

        // Determinar status
        String status = isFallback ? "ERROR" : "RECIBIDA";

        // 4 & 5. Actualizar stats del Tropel y Sector solo si NO es fallback
        if (!isFallback) {
            updateTropelStats(tropel, aiResult.get("severity"));
            updateSectorStability(tropel.getSector(), aiResult.get("signalType"));
            tropelRepository.save(tropel);
            sectorRepository.save(tropel.getSector());
        }

        // 6. Guardar señal
        TropelSignal signal = new TropelSignal();
        signal.setTropel(tropel);
        signal.setGuardian(guardian);
        signal.setSenderTag(req.getSenderTag());
        signal.setRawContent(req.getRawContent());
        signal.setSignalType(aiResult.get("signalType"));
        signal.setSeverity(aiResult.get("severity"));
        signal.setAssignedUnit(aiResult.get("assignedUnit"));
        signal.setRecommendedAction(aiResult.get("recommendedAction"));
        signal.setStatus(status);
        signal.setCreatedAt(Instant.now());
        signal.setUpdatedAt(Instant.now());
        TropelSignal saved = signalRepository.save(signal);

        // 7. Guardar CareResponse
        CareResponse care = new CareResponse();
        care.setSignal(saved);
        care.setResponseCode(SIGNAL_TO_RESPONSE_CODE.getOrDefault(saved.getSignalType(), "ARCHIVE_AND_IGNORE"));
        care.setDescription(saved.getRecommendedAction());
        care.setCreatedAt(Instant.now());
        careResponseRepository.save(care);

        // 8. Publicar evento solo si no es fallback
        if (!isFallback) {
            eventPublisher.publishEvent(new TropelSignalCreatedEvent(saved.getId()));
        }

        return toDTO(saved);
    }

    private void updateTropelStats(Tropel tropel, String severity) {
        int energy = tropel.getEnergyLevel();
        int chaos = tropel.getChaosIndex();
        int mutation = tropel.getMutationStage();

        switch (severity) {
            case "LEVE" -> { energy -= 5; chaos += 5; }
            case "MODERADO" -> { energy -= 10; chaos += 15; }
            case "GRAVE" -> { energy -= 20; chaos += 30; }
            case "CRITICO" -> { energy -= 30; chaos += 45; mutation = Math.min(5, mutation + 1); }
        }

        tropel.setEnergyLevel(Math.max(0, Math.min(100, energy)));
        tropel.setChaosIndex(Math.max(0, Math.min(100, chaos)));
        tropel.setMutationStage(Math.min(5, mutation));

        // vitalState
        int newChaos = tropel.getChaosIndex();
        int newEnergy = tropel.getEnergyLevel();
        if (newChaos >= 80) {
            tropel.setVitalState("CRITICO");
        } else if (newEnergy <= 20) {
            tropel.setVitalState("HAMBRIENTO");
        } else if ("CRITICO".equals(severity)) {
            tropel.setVitalState("MUTANDO");
        } else if ("GRAVE".equals(severity)) {
            tropel.setVitalState("AGITADO");
        }

        tropel.setUpdatedAt(Instant.now());
    }

    private void updateSectorStability(Sector sector, String signalType) {
        if ("FUGA".equals(signalType)) {
            sector.setStabilityLevel(Math.max(0, sector.getStabilityLevel() - 10));
        } else if ("REPRODUCCION_MASIVA".equals(signalType)) {
            sector.setStabilityLevel(Math.max(0, sector.getStabilityLevel() - 15));
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findFiltered(String signalType, String severity, String status,
                                            Long tropelId, Long guardianId,
                                            String from, String to,
                                            int page, int size) {
        Instant fromInstant = from != null ? LocalDate.parse(from).atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        Instant toInstant = to != null ? LocalDate.parse(to).plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant() : null;

        Page<TropelSignal> result = signalRepository.findFiltered(
                signalType, severity, status, tropelId, guardianId, fromInstant, toInstant,
                PageRequest.of(page, size));

        return Map.of(
                "content", result.getContent().stream().map(this::toDTO).toList(),
                "totalElements", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", result.getNumber(),
                "size", result.getSize()
        );
    }

    @Transactional(readOnly = true)
    public TropelSignalResponseDTO findById(Long id) {
        return signalRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una señal con id " + id));
    }

    public TropelSignalResponseDTO toDTO(TropelSignal s) {
        TropelSignalResponseDTO dto = new TropelSignalResponseDTO();
        dto.setId(s.getId());
        dto.setTropelId(s.getTropel().getId());
        dto.setTropelName(s.getTropel().getName());
        dto.setGuardianId(s.getGuardian().getId());
        dto.setGuardianName(s.getGuardian().getDisplayName());
        dto.setSenderTag(s.getSenderTag());
        dto.setRawContent(s.getRawContent());
        dto.setSignalType(s.getSignalType());
        dto.setSeverity(s.getSeverity());
        dto.setAssignedUnit(s.getAssignedUnit());
        dto.setRecommendedAction(s.getRecommendedAction());
        dto.setStatus(s.getStatus());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        return dto;
    }
}