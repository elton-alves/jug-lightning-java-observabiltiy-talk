package erma.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final RestClient restClient;

    // Injecting spring rest client builder bean with pre-configured trace propagation
    public OrderController(RestClient.Builder clientBuilder) {
        restClient = clientBuilder.build();
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        final var amount = request.amount();
        log.info("Received request to create order with amount: ${}", amount);

        log.info("Forwarding payment authorization to downstream service...");
        String paymentUrl = "http://localhost:8080/payments?amount=" + amount;
        String paymentResponse = restClient.post()
                .uri(paymentUrl)
                .retrieve()
                .body(String.class);

        log.info("Order successfully completed.");
        return ResponseEntity.ok("Order Processed! Payment Status: " + paymentResponse);
    }


    // Record para mapear el cuerpo de la petición
    record OrderRequest(double amount, String documentId, String customerName) {}

}

