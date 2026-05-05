package org.test.week06lab01.tropelSignal.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;

import java.time.Instant;

public interface TropelSignalRepository extends JpaRepository<TropelSignal, Long> {
    Page<TropelSignal> findFiltered(String signalType, String severity, String status, Long tropelId, Long guardianId, Instant fromInstant, Instant toInstant, PageRequest of);
}
