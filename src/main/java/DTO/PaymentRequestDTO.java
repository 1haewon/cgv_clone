package DTO;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long reservationId;
    private int amount;
    private String paymentMethod;
}