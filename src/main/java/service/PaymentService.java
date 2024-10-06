package service;

import DTO.PaymentRequestDTO;
import domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public void makePayment(PaymentRequestDTO request) {
        Payment payment = new Payment();
        payment.setReservationId(request.getReservationId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus("결제 완료");

        paymentRepository.save(payment);
    }

    public String getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return payment.getStatus();
    }
}