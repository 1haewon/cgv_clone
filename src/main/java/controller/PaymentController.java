package controller;

import DTO.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.PaymentService;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestDTO request) {
        paymentService.makePayment(request);
        return ResponseEntity.ok("Payment processed successfully");
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<String> getPaymentStatus(@PathVariable Long paymentId) {
        String status = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }
}
