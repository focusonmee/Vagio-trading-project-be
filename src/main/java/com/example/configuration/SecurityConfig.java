package com.example.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF nếu không cần
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 🔥 Gắn cấu hình CORS vào đây
                .authorizeHttpRequests(request ->
                        request.anyRequest().permitAll()); // Cho phép tất cả các yêu cầu
        return httpSecurity.build();
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(Authorization->Authorization.requestMatchers("/api/v1/**").authenticated()
//                .anyRequest().permitAll())
//                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors->cors.configurationSource(corsConfigurationSource()));
//        return httpSecurity.build();
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowCredentials(true); // ✅ Cho phép gửi cookies/tokens giữa các domain
                cfg.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000")); // ✅ Cho phép các domain này
                cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Cho phép các phương thức HTTP
                cfg.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // ✅ Cho phép các headers này
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

}
