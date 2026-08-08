package com.qs.booking.api.dto.external.request.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
public class AccountPostDto {

    private String profilePictureUrl;

    @NotBlank
    @Email
    @Size(max = 254) // Look up RFC 5321 standard
    private String email;

    @NotBlank
    @Size(min= 2, max= 50)
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "First name contains invalid characters")
    private String firstName;

    @NotBlank
    @Size(min= 2, max= 50)
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Last name contains invalid characters")
    private String lastName;

    @NotBlank
    @Size(min= 2, max= 30)
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Nickname contains invalid characters")
    private String nickname;

    @NotBlank
    @Length(min= 14, max = 100)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character"
    )
    @ToString.Exclude
    private String password;
}
