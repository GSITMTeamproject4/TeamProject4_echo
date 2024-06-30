package com.project.echoproject.service;

import com.project.echoproject.dto.order.PaymentRequestDTO;
import com.project.echoproject.dto.order.PaymentResponseDTO;
import com.project.echoproject.entity.Payment;

public interface PaymentService {
    Payment savePayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO verifyPayment(PaymentRequestDTO paymentRequestDTO);
    String getAccessToken() throws Exception;
}
