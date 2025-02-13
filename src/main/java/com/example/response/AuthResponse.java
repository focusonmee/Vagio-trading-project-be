package com.example.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
     String jwt;
     boolean status;
     String message;
     boolean isTwoFactorAuthEnabled;
     String session;
}
