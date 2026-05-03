package org.test.week06lab01.account.application;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.test.week06lab01.account.domain.Account;
import org.test.week06lab01.account.domain.AccountService;
import org.test.week06lab01.account.dto.UpdateUserDto;
import org.test.week06lab01.auth.dto.UserInfoDto;

import java.security.Principal;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/private")
    public ResponseEntity<UserInfoDto> getPrivateInfo() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(new UserInfoDto(principal.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfoDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserDto request
    ) {

        Account updated = accountService.updateUser(
                id,
                request.getFirstName(),
                request.getLastName()
        );

        return ResponseEntity.ok(new UserInfoDto(updated.getUsername()));
    }


}
