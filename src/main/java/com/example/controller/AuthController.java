package com.example.controller;

import com.example.request.UserCreationRequest;
import com.example.response.ApiResponse;
import com.example.response.AuthResponse;
import com.example.response.UserResponse;
import com.example.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> createAccount(@Valid @RequestBody UserCreationRequest request) {
       return ApiResponse.<AuthResponse>builder()
               .result(userService.createAccount(request))
               .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> getUser(@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(userService.signIn(request.getEmail(), request.getPassword()))
                .build();
    }

}
