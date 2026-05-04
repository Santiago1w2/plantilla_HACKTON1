package org.test.week06lab01.tropelSignal.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;

public interface TropelSignalRepository extends JpaRepository<TropelSignal, Long> {
}
