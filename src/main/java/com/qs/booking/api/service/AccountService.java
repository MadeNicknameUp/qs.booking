package com.qs.booking.api.service;

import com.qs.booking.api.dto.AccountRequestDto;
import com.qs.booking.api.dto.AccountResponseDto;
import com.qs.booking.api.error.unit.AccountNotFoundException;
import com.qs.booking.api.mapper.AccountDtoMapper;
import com.qs.booking.store.entity.Account;
import com.qs.booking.store.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDtoMapper accountDtoMapper;
    private final ObjectMapper objectMapper;

    private AccountResponseDto fetchAccount(UUID account_id) {

        Account existingAccount = accountRepository.findById(account_id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s cannot be fetched.".formatted(account_id)));

        return accountDtoMapper.toDto(existingAccount);
    }

    @Transactional
    private AccountResponseDto createAccount(AccountRequestDto accountDto) {

        Account newAccount = accountDtoMapper.toEntity(accountDto);

        validateAccountData(newAccount);

        final Account savedAccount = accountRepository.save(newAccount);

        return accountDtoMapper.toDto(savedAccount);
    }

    @Transactional
    private AccountResponseDto updateAccount(UUID account_id, JsonNode brandNewAccountPart) {

        Account existingAccount = accountRepository.findById(account_id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s cannot be fetched.".formatted(account_id)));

        objectMapper.readerForUpdating(existingAccount).readValue(brandNewAccountPart);

        validateAccountData(existingAccount);

        final Account savedAccount = accountRepository.saveAndFlush(existingAccount);

        return accountDtoMapper.toDto(savedAccount);
    }

    @Transactional
    private void deleteAccount(UUID account_id) {

        accountRepository.findById(account_id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s cannot be fetched.".formatted(account_id)));

        accountRepository.deleteById(account_id);
    }

    private void validateAccountData(Account account) {
        // TODO: Create data validation strategy & requirements
    }
}
