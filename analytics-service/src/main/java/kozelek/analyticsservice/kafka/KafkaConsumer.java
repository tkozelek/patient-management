package kozelek.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@Slf4j
public class KafkaConsumer {

    public KafkaConsumer() {
        log.info("DEBUG: KafkaConsumer bean has been created by Spring!");
    }

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        // need to specify topic to listen to
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            log.info("Analytics consumer listened to event: {}", patientEvent);
        } catch (InvalidProtocolBufferException e) {
            log.error("Error while parsing patient event: {}", e.getMessage());
        }
    }
}
