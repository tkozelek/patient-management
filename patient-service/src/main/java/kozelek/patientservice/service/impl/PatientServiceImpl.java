package kozelek.patientservice.service.impl;

import billing.BillingResponse;
import kozelek.patientservice.dto.PatientRequestDto;
import kozelek.patientservice.dto.PatientResponseDto;
import kozelek.patientservice.entity.Patient;
import kozelek.patientservice.exception.EmailAlreadyExistsException;
import kozelek.patientservice.exception.PatientNotFoundException;
import kozelek.patientservice.grpc.BillingServiceGrpcClient;
import kozelek.patientservice.kafka.KafkaProducer;
import kozelek.patientservice.mapper.PatientMapper;
import kozelek.patientservice.repository.PatientRepository;
import kozelek.patientservice.service.PatientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    @Override
    public List<PatientResponseDto> findAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(patientMapper::toDto).toList();
    }

    @Override
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if (patientRepository.existsByEmail(patientRequestDto.getEmail()))
            throw new EmailAlreadyExistsException("A patient with this email already exists. Email: " + patientRequestDto.getEmail());

        Patient createdPatient = this.patientRepository.save(this.patientMapper.toEntity(patientRequestDto));

        billingServiceGrpcClient.createBillingAccount(
                createdPatient.getId().toString(),
                createdPatient.getName(),
                createdPatient.getEmail()
        );

        this.kafkaProducer.sendEvent(createdPatient);

        return this.patientMapper.toDto(createdPatient);
    }

    @Override
    public PatientResponseDto fullUpdatePatient(UUID id, PatientRequestDto patientRequestDto) {
        Patient patient = this.patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient was not found with id: " + id));

        if (!patient.getEmail().equals(patientRequestDto.getEmail())
                && this.patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistsException("Patient with this email already exists. Email: " + patientRequestDto.getEmail());
        }

        patient.setName(patientRequestDto.getName());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setDateOfBirth(patientRequestDto.getDateOfBirth());

        return this.patientMapper.toDto(this.patientRepository.save(patient));
    }

    @Override
    public void deleteById(UUID id) {
        this.patientRepository.deleteById(id);
    }

    @Override
    public PatientResponseDto findByPatientId(UUID id) {
        Optional<Patient> patientFound = this.patientRepository.findById(id);
        Patient patient = patientFound.orElseThrow(() -> new PatientNotFoundException("Patient was not found with id: " + id));
        return this.patientMapper.toDto(patient);
    }
}
