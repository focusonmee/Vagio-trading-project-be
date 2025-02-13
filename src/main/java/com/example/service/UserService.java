package com.example.service;


import com.example.configuration.JwtProvider;
import com.example.entity.User;
import com.example.enums.USER_ROLE;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.request.UserCreationRequest;
import com.example.response.AuthResponse;
import com.example.response.UserResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public AuthResponse createAccount(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User newUser = userMapper.toUser(request);
        newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("register success");
        return res;
    }

    @Override
    public AuthResponse signIn(String email, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean authenticated = passwordEncoder.matches(password, user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.USER_INPUT_WRONG_PASSWORD);
        }
        Authentication auth = authenticate(email, password);
        String jwt = JwtProvider.generateToken(auth);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Login success");
        return response;

    }


    private Authentication authenticate(String username, String password){
        return new UsernamePasswordAuthenticationToken(username,password);
    }
}

