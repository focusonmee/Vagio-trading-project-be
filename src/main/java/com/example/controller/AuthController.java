package com.example.controller;

import com.example.request.UserCreationRequest;
import com.example.response.ApiResponse;
import com.example.response.UserResponse;
import com.example.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserResponse>createAccount(@Valid @RequestBody UserCreationRequest request){
       return ApiResponse.<UserResponse>builder()
                .result(userService.createAccount(request))
                .build();
    }

    @GetMapping("/login")
    public ApiResponse<UserResponse>getUser(@Valid @RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByEmail(request.getEmail()))
                .build();
    }

}
