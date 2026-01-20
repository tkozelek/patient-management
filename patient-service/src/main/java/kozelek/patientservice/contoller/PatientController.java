package kozelek.patientservice.contoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import kozelek.patientservice.dto.PatientRequestDto;
import kozelek.patientservice.dto.PatientResponseDto;
import kozelek.patientservice.dto.validators.CreationValidator;
import kozelek.patientservice.grpc.BillingServiceGrpcClient;
import kozelek.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        log.info("Get all patients was requested.");

        List<PatientResponseDto> patientsDtos = this.patientService.findAllPatients();

        log.info("Returning {} patients", patientsDtos.size());

        return ResponseEntity.ok(patientsDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by id")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable UUID id) {
        log.info("Get patient with {} was requested.", id);

        PatientResponseDto patientDto = this.patientService.findByPatientId(id);

        return ResponseEntity.ok(patientDto);
    }

    @PostMapping
    @Operation(summary = "Create patient")
    public ResponseEntity<PatientResponseDto> createPatient(@Validated({Default.class, CreationValidator.class}) @RequestBody PatientRequestDto patientRequestDto) {
        log.info("Creating a patient {} was requested.", patientRequestDto.getName());

        PatientResponseDto patientResponseDto = this.patientService.createPatient(patientRequestDto);

        return ResponseEntity.ok(patientResponseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id, @Valid @RequestBody PatientRequestDto patientRequestDto) {
        log.info("Updating a patient {} was requested.", patientRequestDto.getName());

        PatientResponseDto patientDto = this.patientService.fullUpdatePatient(id, patientRequestDto);

        return ResponseEntity.ok(patientDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        log.info("Deleting a patient {} was requested.", id);
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
