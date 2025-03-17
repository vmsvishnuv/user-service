package com.myproject.user_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.myproject.user_service.util.StringConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String UserId;

    @NotBlank(message = EMPTY_USERNAME)
    private String userName;

    //@Email(message = "Provide a valid email") // allowing email without .com
    @Pattern(
            regexp = EMAIL_PATTERN,
            message = INVALID_EMAIL
    )
    @NotBlank(message = EMPTY_EMAIL)
    private String email;

    @NotBlank(message = EMPTY_ADDRESS)
    private String address;
}
