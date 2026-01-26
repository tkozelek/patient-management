package kozelek.authservice.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final String token;
}
