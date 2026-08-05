package com.qs.booking.api.contoller;

import com.qs.booking.api.dto.external.AccountRequestDto;
import com.qs.booking.api.dto.external.AccountResponseDto;
import com.qs.booking.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{account_id}")
    public ResponseEntity<AccountResponseDto> fetchAccount(@PathVariable UUID accountId) {

        return ResponseEntity.ok(accountService.fetchAccount(accountId));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountRequestDto));
    }

    @PatchMapping("/{account_id}")
    public ResponseEntity<AccountResponseDto> createAccount(@PathVariable UUID accountId, @RequestBody JsonNode accountRequestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.updateAccount(accountId, accountRequestDto));
    }

    @DeleteMapping("/{account_id}")
    public HttpStatus deleteAccount(@PathVariable UUID accountId) {

        accountService.deleteAccount(accountId);

        return HttpStatus.OK;
    }
}
