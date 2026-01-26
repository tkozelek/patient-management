package kozelek.authservice.service;

import kozelek.authservice.dto.LoginRequestDto;

import java.util.Optional;

public interface AuthService {
    Optional<String> authenticate(LoginRequestDto loginRequestDto);
    boolean validateToken(String token);
}
