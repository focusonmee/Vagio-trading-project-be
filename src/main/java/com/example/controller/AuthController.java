package com.example.controller;

import com.example.domain.VerificationType;
import com.example.entity.User;
import com.example.request.ForgotPasswordTokenRequest;
import com.example.request.ResetPasswordRequest;
import com.example.request.UserCreationRequest;
import com.example.response.ApiResponse;
import com.example.response.AuthResponse;
import com.example.response.ResetPasswordResponse;
import com.example.service.EmailService;
import com.example.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final EmailService emailService;


    // users register new account
    @PostMapping("/signup")
    public ApiResponse<AuthResponse> createAccount(@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(userService.createAccount(request))
                .build();
    }

    // users login into their account
    @PostMapping("/login")
    public ApiResponse<AuthResponse> getUser(@Valid @RequestBody UserCreationRequest request) throws MessagingException {
        return ApiResponse.<AuthResponse>builder()
                .result(userService.login(request.getEmail(), request.getPassword()))
                .build();
    }

    //Client submits an OTP (One-Time Password) for login verification.
    //The otp and id (user ID) are passed to userService.verifyLoginOtp(otp, id).
    //If OTP is correct, login is approved; otherwise, an error is returned.
    @PostMapping("/two-factor/otp/{otp}")
    public ApiResponse<AuthResponse> verifyLoginOtp(
            @PathVariable String otp
            , @RequestParam String id) {

        return ApiResponse.<AuthResponse>builder()
                .result(userService.verifyLoginOtp(otp, id))
                .build();
    }

    //Client requests profile data by sending a JWT in the Authorization header.
    //userService.findUserbyJwt(jwt) extracts the user from the token.
    //The user details are returned inside an ApiResponse<User>.
    @GetMapping("/users/profile")
    public ApiResponse<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserbyJwt(jwt);
        return ApiResponse.<User>builder()
                .result(user)
                .build();

    }

    @GetMapping("/users/{id}")
    public ApiResponse<User> getUserById(@RequestHeader("Authorization") String jwt,
                                         @PathVariable Long id) {
        User user = userService.findUserById(id);
        return ApiResponse.<User>builder()
                .result(user)
                .build();

    }


    //Client sends a request to enable 2FA and provides an OTP.
    //The user is identified using userService.findUserbyJwt(jwt).
    //userService.enableTwoFactor(jwt, otp) verifies OTP and enables 2FA for the user.
    //Updated user details are returned.
    @PatchMapping("/users/enable-two-factor/verify-otp/{otp}")
    public ApiResponse<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
                                                           @PathVariable String otp) {
        User user = userService.findUserbyJwt(jwt);
        return ApiResponse.<User>builder()
                .result(userService.findUserbyJwt(jwt))
                .build();
    }


    //Client requests an OTP for account verification (e.g., email verification).
    //The user is identified using the JWT token.
    //The verificationType determines the OTP purpose (e.g., EMAIL, PHONE).
    //The OTP is generated and sent to the user (via email/SMS).
    @PostMapping("/users/verification/{verificationType}/send-otp")
    public ApiResponse<String> sendVerificationOtp(
            @RequestHeader("Authorization")
            String jwt,
            @PathVariable VerificationType verificationType
    ) throws Exception {
        return ApiResponse.<String>builder()
                .result(userService.sendVerificationOtp(jwt, verificationType))
                .build();
    }

    // forgot passowrd
    @PostMapping("/auth/users/reset-password/send-otp")
    public ApiResponse<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPasswordTokenRequest req
    ) throws Exception {

        return ApiResponse.<AuthResponse>builder()
                .result(userService.sendForgotPasswordOtp(req))
                .build();
    }

    //
    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ApiResponse<ResetPasswordResponse>resetPassword(
            @RequestParam String id,
            @RequestHeader("Authorization") String jwt,
            @RequestBody ResetPasswordRequest request
            ) {
        User user = userService.findUserbyJwt(jwt);
        return ApiResponse.<ResetPasswordResponse>builder()
                .result(userService.resetPassword(id,jwt,request))
                .build();
    }
}
