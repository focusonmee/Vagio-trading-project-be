package com.example.service;


import com.example.request.UserCreationRequest;
import com.example.response.UserResponse;

public interface IUserService {
    public UserResponse createAccount (UserCreationRequest request);

    public UserResponse getUserByEmail(String email) ;
}
