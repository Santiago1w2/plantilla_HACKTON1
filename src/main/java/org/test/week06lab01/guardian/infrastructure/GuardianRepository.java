package org.test.week06lab01.guardian.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.week06lab01.guardian.domain.Guardian;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {
    boolean existsByEmail(String email);
}
