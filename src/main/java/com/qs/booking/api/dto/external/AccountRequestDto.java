package com.qs.booking.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountRequestDto {

    private String profilePictureUrl;

    private String email;

    private String firstName;

    private String lastName;

    private String nickname;

    // password hash
    private String password;
}
