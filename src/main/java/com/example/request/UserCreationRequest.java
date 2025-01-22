package com.example.request;

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
public class UserCreationRequest {

    @Size(min=3, message = "USERNAME_INVALID")
    String fullName;

    @Email
    String email;

    @Size(min=8, message = "PASSWORD_INVALID")
     String password;

}
