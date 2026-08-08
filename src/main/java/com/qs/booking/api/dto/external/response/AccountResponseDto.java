package com.qs.booking.api.dto.external.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountResponseDto {

    // hashed id
    private String id;

    private String profilePictureUrl;

    private String email;

    private String firstName;

    private String lastName;

    private String nickname;

    // TODO: Needs to be inspected one more time.
//    private String role;

    private String createdAt;

    private String updatedAt;
}