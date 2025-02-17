package com.example.request;


import com.example.domain.VerificationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ForgotPasswordTokenRequest {

    String sendTo;
    VerificationType verificationType;

}

