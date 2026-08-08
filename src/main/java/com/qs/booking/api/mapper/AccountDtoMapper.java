package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.request.post.AccountPostDto;
import com.qs.booking.api.dto.external.response.AccountResponseDto;
import com.qs.booking.api.error.unit.InvalidParameterException;
import com.qs.booking.store.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoMapper {

    public AccountResponseDto toDto(Account account) {

        return AccountResponseDto
                .builder()
                .id(account.getId().toString())
                .profilePictureUrl(account.getProfilePictureUrl())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .nickname(account.getNickname())
//                .role(account.getRole().getTitle())
                .createdAt(account.getCreatedAt().toString())
                .updatedAt(account.getUpdatedAt().toString())
                .build();
    }

    public Account toEntity(AccountPostDto accountDto, String errorPath) {

        Account account = new Account();

        try {
            account.setProfilePictureUrl(accountDto.getProfilePictureUrl());
            account.setEmail(accountDto.getEmail());
            account.setFirstName(accountDto.getFirstName());
            account.setLastName(accountDto.getLastName());
            account.setNickname(accountDto.getNickname());
            account.setPassword(accountDto.getPassword());

        } catch (Exception ex) {
            throw new InvalidParameterException(
                    "Account object cannot be created due to invalid data.",
                    errorPath
                    );
        }

        return account;
    }
}
