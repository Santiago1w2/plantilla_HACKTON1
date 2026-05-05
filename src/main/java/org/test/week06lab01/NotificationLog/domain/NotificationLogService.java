package org.test.week06lab01.NotificationLog.domain;

import org.test.week06lab01.NotificationLog.domain.NotificationLog;
import org.test.week06lab01.NotificationLog.domain.NotifStatus;
import org.test.week06lab01.NotificationLog.infrastructure.NotificactionLogRepository;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class NotificationLogService {

    private final NotificactionLogRepository notificationLogRepository;

    public NotificationLogService(NotificactionLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    public void logSuccess(TropelSignal signal, String recipientEmail, String subject) {
        NotificationLog log = new NotificationLog();
        log.setSignal(signal);
        log.setRecipientEmail(recipientEmail);
        log.setSubject(subject);
        log.setNotifStatus(NotifStatus.SENT);
        log.setErrorMessage(null);
        log.setSentAt(Instant.now());
        log.setCreatedAt(Instant.now());
        notificationLogRepository.save(log);
    }

    public void logFailure(TropelSignal signal, String recipientEmail, String subject, String errorMessage) {
        NotificationLog log = new NotificationLog();
        log.setSignal(signal);
        log.setRecipientEmail(recipientEmail);
        log.setSubject(subject);
        log.setNotifStatus(NotifStatus.FAILED);
        log.setErrorMessage(errorMessage);
        log.setSentAt(null);
        log.setCreatedAt(Instant.now());
        notificationLogRepository.save(log);
    }
}