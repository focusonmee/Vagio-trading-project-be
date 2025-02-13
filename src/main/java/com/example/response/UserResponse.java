package com.example.response;

import com.example.domain.TwoFactorAuth;
import com.example.enums.USER_ROLE;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    long id;

    @Size(min=3, message = "USERNAME_INVALID")
    String fullName;

    @Email
    String email;

    String role ;




}
