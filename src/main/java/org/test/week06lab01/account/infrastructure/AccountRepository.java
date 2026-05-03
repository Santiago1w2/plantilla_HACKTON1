package org.test.week06lab01.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.week06lab01.account.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
