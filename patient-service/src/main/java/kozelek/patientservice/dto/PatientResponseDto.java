package kozelek.patientservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PatientResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}
