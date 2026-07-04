package com.retailplatform.security.service;

import com.retailplatform.common.exception.BusinessException;
import com.retailplatform.security.dto.LoginRequest;
import com.retailplatform.security.dto.RegisterRequest;
import com.retailplatform.security.dto.TokenResponse;
import com.retailplatform.security.entity.Role;
import com.retailplatform.security.entity.User;
import com.retailplatform.security.repository.RoleRepository;
import com.retailplatform.security.repository.UserRepository;
import com.retailplatform.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("USER_EXISTS", "Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email already registered");
        }

        Role defaultRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new BusinessException("ROLE_MISSING", "Default role not seeded"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("INVALID_CREDENTIALS", "Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("INVALID_CREDENTIALS", "Invalid username or password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken, jwtTokenProvider.getAccessTokenValiditySeconds());
    }
}
