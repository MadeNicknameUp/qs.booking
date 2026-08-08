package com.qs.booking.api.service;

import com.qs.booking.api.dto.external.request.patch.AccountPatchDto;
import com.qs.booking.api.dto.external.request.post.AccountPostDto;
import com.qs.booking.api.dto.external.response.AccountResponseDto;
import com.qs.booking.api.error.unit.AccountNotFoundException;
import com.qs.booking.api.error.unit.InvalidParameterException;
import com.qs.booking.api.mapper.AccountDtoMapper;
import com.qs.booking.store.model.Account;
import com.qs.booking.store.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDtoMapper accountDtoMapper;

    public AccountResponseDto fetchAccount(UUID accountId) {

        Account fetchedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id: %s cannot be fetched.".formatted(accountId),
                        "/api/v1/accounts/%s".formatted(accountId)
                ));

        return accountDtoMapper.toDto(fetchedAccount);
    }

    public Optional<Account> internalFetchAccount(UUID account_id) {

        return accountRepository.findById(account_id);
    }

    @Transactional
    public AccountResponseDto createAccount(AccountPostDto accountDto) {

        Account newAccount = accountDtoMapper.toEntity(accountDto, "api/v1/accounts");

        final Account savedAccount = accountRepository.saveAndFlush(newAccount);

        return accountDtoMapper.toDto(savedAccount);
    }

    @Transactional
    public AccountResponseDto updateAccount(UUID accountId, AccountPatchDto accountPatchDto) {

        String errorPath = String.format("api/v1/accounts/%s", accountId);

        Account fetchedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id: %s cannot be fetched.".formatted(accountId),
                        errorPath
                ));

        validatePatchRequest(fetchedAccount,  accountPatchDto, errorPath);

        final Account savedAccount = accountRepository.saveAndFlush(fetchedAccount);

        return accountDtoMapper.toDto(savedAccount);
    }

    @Transactional
    public void deleteAccount(UUID accountId) {

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id: %s cannot be fetched.".formatted(accountId),
                        "/api/v1/accounts/%s".formatted(accountId)
                ));

        accountRepository.deleteById(accountId);
    }

    public void validatePatchRequest(Account account, AccountPatchDto accountPatchDto, String errorPath) {

        accountPatchDto.getEmail().ifPresent((email) -> {
            if (email.isBlank()) {
                throw new InvalidParameterException("Invalid email: Entry cannot be empty.", errorPath);
            } else if (email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
                throw new InvalidParameterException("Invalid email: Entry contains invalid characters.", errorPath);
            } else if (email.length() > 254) {
                throw new InvalidParameterException("Invalid email: Entry is way to long. Max length is 254.", errorPath);
            } else if (accountRepository.findByEmail(email).isPresent()) {
                throw new InvalidParameterException("Invalid email: Email address already exists.", errorPath);
            } else {
                account.setEmail(email);
            }
        });
        accountPatchDto.getFirstName().ifPresent((firstName) -> {
            if (firstName.isBlank()) {
                throw new InvalidParameterException("Invalid first_name: Entry cannot be empty.", errorPath);
            } else if (!firstName.matches("^[\\p{L} '-]+$")) {
                throw new InvalidParameterException("Invalid first_name: Entry contains invalid characters.", errorPath);
            } else if (firstName.length() < 2) {
                throw new InvalidParameterException("Invalid first_name: Entry is way to short. Min length is 2.", errorPath);
            } else if (firstName.length() > 50) {
                throw new InvalidParameterException("Invalid first_name: Entry is way to long. Max length is 50.", errorPath);
            } else {
                account.setFirstName(firstName);
            }
        });
        accountPatchDto.getLastName().ifPresent((lastName) -> {
            if (lastName.isBlank()) {
                throw new InvalidParameterException("Invalid last_name: Entry cannot be empty.", errorPath);
            } else if (!lastName.matches("^[\\p{L} '-]+$")) {
                throw new InvalidParameterException("Invalid last_name: Entry contains invalid characters.", errorPath);
            } else if (lastName.length() < 2) {
                throw new InvalidParameterException("Invalid last_name: Entry is way to short. Min length is 2.", errorPath);
            } else if (lastName.length() > 50) {
                throw new InvalidParameterException("Invalid last_name: Entry is way to long. Max length is 50.", errorPath);
            } else {
                account.setFirstName(lastName);
            }
        });
        accountPatchDto.getNickname().ifPresent((nickname) -> {
            if (nickname.isBlank()) {
                throw new InvalidParameterException("Invalid nickname: Entry cannot be empty.", errorPath);
            } else if (!nickname.matches("^[a-zA-Z0-9_.-]+$")) {
                throw new InvalidParameterException("Invalid nickname: Entry contains invalid characters.", errorPath);
            } else if (nickname.length() < 2) {
                throw new InvalidParameterException("Invalid nickname: Entry is way to short. Min length is 2.", errorPath);
            } else if (nickname.length() > 30) {
                throw new InvalidParameterException("Invalid nickname: Entry is way to long. Max length is 50.", errorPath);
            } else {
                account.setNickname(nickname);
            }
        });
        accountPatchDto.getProfilePictureUrl().ifPresent((profilePictureUrl) -> {
            if (profilePictureUrl.isBlank()) {
                throw new InvalidParameterException("Invalid profile_picture_url: Entry cannot be empty.", errorPath);
            } else {
                account.setProfilePictureUrl(profilePictureUrl);
            }
        });
    }
}
