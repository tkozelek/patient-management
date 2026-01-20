package kozelek.patientservice.mapper;

import kozelek.patientservice.dto.PatientRequestDto;
import kozelek.patientservice.dto.PatientResponseDto;
import kozelek.patientservice.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponseDto toDto(Patient patient);
    Patient toEntity(PatientRequestDto patientRequestDto);
}
