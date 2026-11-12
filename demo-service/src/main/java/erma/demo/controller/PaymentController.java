package erma.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);


    @PostMapping("/payments")
    public ResponseEntity<String> processPayment(@RequestParam double amount) {
        log.info("Downstream Payment Service invoked for amount: ${}", amount);

        if (amount > 1000) {
            log.error("Payment fraud check failed! Amount ${} exceeds limit.", amount);
            throw new IllegalArgumentException("Transaction limit exceeded! Potential fraud.");
        }

        return ResponseEntity.ok("APPROVED");
    }
}
