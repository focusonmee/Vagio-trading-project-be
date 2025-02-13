package com.example.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Authentication errors (AUTH)
    AUTH_INVALID_CREDENTIALS(1001, "Invalid credentials", HttpStatus.FORBIDDEN),
    AUTH_USER_LOCKED(1002, "User account is locked", HttpStatus.LOCKED),
    AUTH_TOKEN_EXPIRED(1003, "Authentication token has expired", HttpStatus.UNAUTHORIZED),

    // User errors (USER)
    USER_EXISTED(2001, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND(2002, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(2003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    FIRSTNAME_INVALID(2004, "FirstName must be at least 3 characters", HttpStatus.BAD_REQUEST),
    LASTNAME_INVALID(2005, "LastName must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(2006, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2007, "User does not exist", HttpStatus.NOT_FOUND),
    USER_UNAUTHENTICATED(2008, "User Unauthenticated", HttpStatus.FORBIDDEN),
    USER_UNAUTHORIZED(2009, "User Unauthorized", HttpStatus.FORBIDDEN),
    USER_INPUT_WRONG_PASSWORD (2010,"Wrong email or password",HttpStatus.BAD_REQUEST),

    // Validation errors (VALIDATION)
    VALIDATION_INVALID_INPUT(3001, "Invalid input", HttpStatus.BAD_REQUEST),
    VALIDATION_FIELD_REQUIRED(3002, "Required field is missing", HttpStatus.BAD_REQUEST),

    // System errors (SYSTEM)
    SYS_DATABASE_ERROR(5001, "Database error", HttpStatus.INTERNAL_SERVER_ERROR),
    SYS_UNEXPECTED_ERROR(5002, "Unexpected system error", HttpStatus.INTERNAL_SERVER_ERROR),
    JSON_WRONG_FORMAT(5003, "Json wrong format", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}

