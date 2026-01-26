package kozelek.authservice.service.impl;

import io.jsonwebtoken.JwtException;
import kozelek.authservice.dto.LoginRequestDto;
import kozelek.authservice.service.AuthService;
import kozelek.authservice.service.UserService;
import kozelek.authservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Optional<String> authenticate(LoginRequestDto loginRequestDto) {
        return userService
                .findByEmail(loginRequestDto.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDto.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
    }

    @Override
    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            log.error("Invalid token provided");
            return false;
        }
    }
}
