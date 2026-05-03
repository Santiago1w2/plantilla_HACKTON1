package org.test.week06lab01.account.domain;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.test.week06lab01.account.infrastructure.AccountRepository;

@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository
                .findByEmail(username)
                .orElseThrow();
    }

    public Account updateUser(Long id, String firstName, String lastName) {
                Account account = accountRepository.findById(id)
                .orElseThrow();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        return accountRepository.save(account);
    }

}
