package org.test.week06lab01.tropelSignal.listener;

import org.test.week06lab01.NotificationLog.domain.NotificationLogService;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;
import org.test.week06lab01.tropelSignal.events.TropelSignalCreatedEvent;
import org.test.week06lab01.tropelSignal.infrastructure.TropelSignalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

@Component
public class TropelSignalNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(TropelSignalNotificationListener.class);

    private final JavaMailSender mailSender;
    private final NotificationLogService notificationLogService;
    private final TropelSignalRepository tropelSignalRepository;

    public TropelSignalNotificationListener(JavaMailSender mailSender,
                                            NotificationLogService notificationLogService,
                                            TropelSignalRepository tropelSignalRepository) {
        this.mailSender = mailSender;
        this.notificationLogService = notificationLogService;
        this.tropelSignalRepository = tropelSignalRepository;
    }

    @Async("tropelTaskExecutor")
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSignalCreated(TropelSignalCreatedEvent event) {
        TropelSignal signal = tropelSignalRepository.findById(event.getSignalId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Señal no encontrada: " + event.getSignalId()));

        // Actualizar status a PROCESANDO
        signal.setStatus("PROCESANDO");
        signal.setUpdatedAt(Instant.now());
        tropelSignalRepository.save(signal);

        String recipientEmail = signal.getGuardian().getNotificationEmail();
        String subject = "[TROPELCARE] " + signal.getSignalType()
                + " detectada en " + signal.getTropel().getName()
                + " | Severidad " + signal.getSeverity();

        String body = buildEmailBody(signal);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            // Éxito
            signal.setStatus("ATENDIDA");
            signal.setUpdatedAt(Instant.now());
            tropelSignalRepository.save(signal);

            notificationLogService.logSuccess(signal, recipientEmail, subject);

        } catch (MailException e) {
            // Fallo SMTP
            signal.setStatus("ERROR");
            signal.setUpdatedAt(Instant.now());
            tropelSignalRepository.save(signal);

            notificationLogService.logFailure(signal, recipientEmail, subject, e.getMessage());

            log.error("Fallo al enviar correo para señal {}: {}", signal.getId(), e.getMessage());
        }

        // Log obligatorio en consola
        log.info("[TROPEL-LOG] Signal ID: {} | Tropel: {} | Type: {} | Severity: {} | Unit: {} | Thread: {} | Status: {}",
                signal.getId(),
                signal.getTropel().getName(),
                signal.getSignalType(),
                signal.getSeverity(),
                signal.getAssignedUnit(),
                Thread.currentThread().getName(),
                signal.getStatus());
    }

    private String buildEmailBody(TropelSignal signal) {
        return "Hola " + signal.getGuardian().getDisplayName() + ",\n\n"
                + "Tu Tropel ha emitido una señal que requiere atención.\n\n"
                + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                + "Señal ID         : #" + signal.getId() + "\n"
                + "Tropel           : " + signal.getTropel().getName()
                + " (" + signal.getTropel().getSpecies() + ")\n"
                + "Tipo de señal    : " + signal.getSignalType() + "\n"
                + "Severidad        : " + signal.getSeverity() + "\n"
                + "Unidad asignada  : " + signal.getAssignedUnit() + "\n"
                + "Acción sugerida  : " + signal.getRecommendedAction() + "\n"
                + "Estado vital     : " + signal.getTropel().getVitalState() + "\n"
                + "Nivel de energía : " + signal.getTropel().getEnergyLevel() + "/100\n"
                + "Índice de caos   : " + signal.getTropel().getChaosIndex() + "/100\n"
                + "Etapa mutación   : " + signal.getTropel().getMutationStage() + "/5\n"
                + "Registrada       : " + signal.getCreatedAt() + "\n"
                + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n"
                + "Señal original:\n"
                + "\"" + signal.getRawContent() + "\"\n\n"
                + "— TropelCare Signal Engine, Tuckersoft";
    }
}