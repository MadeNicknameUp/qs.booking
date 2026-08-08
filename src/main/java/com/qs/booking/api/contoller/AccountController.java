package com.qs.booking.api.contoller;

import com.qs.booking.api.dto.external.request.patch.AccountPatchDto;
import com.qs.booking.api.dto.external.request.post.AccountPostDto;
import com.qs.booking.api.dto.external.response.AccountResponseDto;
import com.qs.booking.api.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{account_id}")
    public ResponseEntity<AccountResponseDto> fetchAccount(@PathVariable(name= "account_id") UUID accountId) {

        return ResponseEntity.ok(accountService.fetchAccount(accountId));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountPostDto accountPostDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountPostDto));
    }

    @PatchMapping("/{account_id}")
    public ResponseEntity<AccountResponseDto> createAccount(@PathVariable(name= "account_id") UUID accountId,@RequestBody AccountPatchDto accountPatchDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.updateAccount(accountId, accountPatchDto));
    }

    @DeleteMapping("/{account_id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(name= "account_id") UUID accountId) {

        accountService.deleteAccount(accountId);

        return ResponseEntity.noContent().build();
    }
}
