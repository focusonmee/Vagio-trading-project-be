package com.example.service;


import com.example.entity.User;
import com.example.enums.USER_ROLE;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.request.UserCreationRequest;
import com.example.response.UserResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;
    UserMapper userMapper;
    @Override
    public UserResponse createAccount(UserCreationRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
                throw new AppException(ErrorCode.USER_EXISTED);
        }
       User newUser = userMapper.toUser(request);
        newUser.setRole(USER_ROLE.ROLE_ADMIN);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
         userRepository.save(newUser);
        return userMapper.toUserResponse(newUser);


    }

    @Override
    public UserResponse getUserByEmail(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new AppException((ErrorCode.USER_NOT_FOUND));
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return  userMapper.toUserResponse(user);
    }


}
