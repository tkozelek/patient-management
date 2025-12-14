package kozelek.patientservice.service;

import jakarta.validation.Valid;
import kozelek.patientservice.dto.PatientRequestDto;
import kozelek.patientservice.dto.PatientResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PatientService {
    List<PatientResponseDto> findAllPatients();

    PatientResponseDto createPatient(@Valid PatientRequestDto patientRequestDto);

    PatientResponseDto fullUpdatePatient(UUID id, PatientRequestDto patientRequestDto);

    void deleteById(UUID id);

    PatientResponseDto findByPatientId(UUID id);
}
