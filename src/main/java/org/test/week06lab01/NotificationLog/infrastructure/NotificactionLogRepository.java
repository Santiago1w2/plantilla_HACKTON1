package org.test.week06lab01.NotificationLog.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.week06lab01.NotificationLog.domain.NotificationLog;
import org.test.week06lab01.NotificationLog.domain.NotificationLogService;

public interface NotificactionLogRepository extends JpaRepository<NotificationLog, Long> {
}
