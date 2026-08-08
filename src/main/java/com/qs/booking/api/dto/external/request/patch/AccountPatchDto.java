package com.qs.booking.api.dto.external.request.patch;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class AccountPatchDto {

    private JsonNullable<String> profilePictureUrl;

    private JsonNullable<String> email;

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    private JsonNullable<String> nickname;
}
