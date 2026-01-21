package kozelek.patientservice.kafka;

import kozelek.patientservice.entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEvent(Patient patient) {
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            this.kafkaTemplate.send("patient", patientEvent.toByteArray());
            log.info("Sent patient event: {}", patientEvent);
        } catch (Exception e) {
            log.error("Error sending PATIENT_CREATED evrnt: {}", e.getMessage());
        }
    }
}
