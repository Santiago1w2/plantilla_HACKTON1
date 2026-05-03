package org.test.week06lab01.auth.domain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.week06lab01.account.domain.Account;
import org.test.week06lab01.account.domain.Role;
import org.test.week06lab01.account.infrastructure.AccountRepository;
import org.test.week06lab01.auth.components.JwtService;
import org.test.week06lab01.auth.dto.SignUpRequest;
import org.test.week06lab01.auth.dto.TokenResponse;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse signUp(SignUpRequest request){
        Account account = accountRepository.save(
                new Account(
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFirstName(),
                        request.getLastName(),
                        Role.ROLE_USER
                )
        );
        var token = jwtService.generateToken(account);
        return new TokenResponse(token);
    }

    public TokenResponse signIn(String username, String password){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        var account = accountRepository.findByEmail(username).orElseThrow();
        var token = jwtService.generateToken(account);
        return new TokenResponse(token);
    }
}
