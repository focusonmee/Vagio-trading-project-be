package com.example.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 🔹 Authentication errors (AUTH)
    AUTH_INVALID_CREDENTIALS(1001, "Invalid credentials", HttpStatus.FORBIDDEN),
    AUTH_USER_LOCKED(1002, "User account is locked", HttpStatus.LOCKED),
    AUTH_TOKEN_EXPIRED(1003, "Authentication token has expired", HttpStatus.UNAUTHORIZED),

    // 🔹 User errors (USER)
    USER_ALREADY_EXISTS(2001, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND(2002, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_TOO_SHORT(2003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    FIRSTNAME_TOO_SHORT(2004, "First name must be at least 3 characters", HttpStatus.BAD_REQUEST),
    LASTNAME_TOO_SHORT(2005, "Last name must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_WEAK(2006, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_DOES_NOT_EXIST(2007, "User does not exist", HttpStatus.NOT_FOUND),
    USER_UNAUTHENTICATED(2008, "User is not authenticated", HttpStatus.UNAUTHORIZED),
    USER_UNAUTHORIZED(2009, "User is not authorized", HttpStatus.FORBIDDEN),
    USER_WRONG_PASSWORD(2010, "Incorrect email or password", HttpStatus.BAD_REQUEST),

    // 🔹 Verification errors (VERIFICATION)
    VERIFICATION_CODE_NOT_FOUND(4001, "Verification code not found", HttpStatus.NOT_FOUND),
    INVALID_VERIFICATION_CODE(4002, "Invalid verification code", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED(4003, "Verification code has expired", HttpStatus.GONE),
    INVALID_OTP(4004, "Invalid OTP", HttpStatus.BAD_REQUEST),


    // 🔹 Validation errors (VALIDATION)
    VALIDATION_INVALID_INPUT(3001, "Invalid input", HttpStatus.BAD_REQUEST),
    VALIDATION_FIELD_REQUIRED(3002, "Required field is missing", HttpStatus.BAD_REQUEST),

    // 🔹 System errors (SYSTEM)
    SYS_DATABASE_ERROR(5001, "Database error", HttpStatus.INTERNAL_SERVER_ERROR),
    SYS_UNEXPECTED_ERROR(5002, "Unexpected system error", HttpStatus.INTERNAL_SERVER_ERROR),
    JSON_WRONG_FORMAT(5003, "Invalid JSON format", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
