package kozelek.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kozelek.authservice.dto.LoginRequestDto;
import kozelek.authservice.dto.LoginResponseDto;
import kozelek.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto ) {
        log.info("Login requested for user: {}", loginRequestDto.getEmail());
        Optional<String> tokenOptional = authService.authenticate(loginRequestDto);

        if (tokenOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @Operation(summary = "Validate token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        // Authorization: Bearer <token>

        if (authHeader == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
