package com.example.mapper;


import com.example.entity.User;
import com.example.request.UserCreationRequest;
import com.example.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")

public interface UserMapper {
    UserResponse toUserResponse (User user);

    User toUser (UserCreationRequest request);
}

