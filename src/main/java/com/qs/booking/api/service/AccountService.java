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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDtoMapper accountDtoMapper;
    private final ObjectMapper objectMapper;

    public AccountResponseDto fetchAccount(UUID accountId) {

        Account fetchedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s cannot be fetched.".formatted(accountId)));

        return accountDtoMapper.toDto(fetchedAccount);
    }

    public Optional<Account> internalFetchAccount(UUID account_id) {

        return accountRepository.findById(account_id);
    }

    @Transactional
    public AccountResponseDto createAccount(AccountRequestDto accountDto) {

        Account newAccount = accountDtoMapper.toEntity(accountDto);

        validateAccountData(newAccount);

        final Account savedAccount = accountRepository.saveAndFlush(newAccount);

        return accountDtoMapper.toDto(savedAccount);
    }

    @Transactional
    public AccountResponseDto updateAccount(UUID accountId, JsonNode brandNewAccountPart) {

        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s cannot be fetched.".formatted(accountId)));

        objectMapper.readerForUpdating(existingAccount).readValue(brandNewAccountPart);

        validateAccountData(existingAccount);

        final Account savedAccount = accountRepository.saveAndFlush(existingAccount);

        return accountDtoMapper.toDto(savedAccount);
    }

    @Transactional
    public void deleteAccount(UUID accountId) {

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s cannot be fetched.".formatted(accountId)));

        accountRepository.deleteById(accountId);
    }

    private void validateAccountData(Account account) {
        // TODO: Create data validation strategy & requirements
    }
}
