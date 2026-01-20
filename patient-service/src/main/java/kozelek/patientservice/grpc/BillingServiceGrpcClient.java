package kozelek.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillingServiceGrpcClient {
    // synchronous grpc calls
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    // zoberieme adresy z application.properties
    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort
    ) {
        log.info("Initializing BillingServiceGrpcClient at {}:{}", serverAddress, serverPort);

        // cosi ako http klient ale pre grpc
        // plaintext vypne sifrovanie
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();


        try {
            BillingResponse response = this.blockingStub.createBillingAccount(request);
            log.info("BillingResponse from createBillingAccount via GRPC is {}", response);
            return response;
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to call BillingService via gRPC", e);
        } catch (Exception e) {
            log.error("Unexpected error while calling BillingService", e);
            throw new RuntimeException("Unexpected error in gRPC client", e);
        }
    }
}
