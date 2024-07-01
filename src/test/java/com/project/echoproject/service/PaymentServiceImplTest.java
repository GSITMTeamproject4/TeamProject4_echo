package com.project.echoproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.echoproject.dto.order.PaymentRequestDTO;
import com.project.echoproject.dto.order.PaymentResponseDTO;
import com.project.echoproject.entity.Payment;
import com.project.echoproject.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String apiSecret;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePayment() {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO();
        requestDTO.setImp_uid("imp_12345678");
        requestDTO.setMerchant_uid("merchant_12345678");
        requestDTO.setName("Test Payment");
        requestDTO.setAmount(10000);
        requestDTO.setBuyer_email("test@example.com");
        requestDTO.setBuyer_name("Tester");
        requestDTO.setBuyer_tel("010-1234-5678");
        requestDTO.setBuyer_addr("123 Test St");
        requestDTO.setBuyer_postcode("12345");

        Payment payment = new Payment();
        payment.setImp_uid(requestDTO.getImp_uid());
        payment.setMerchant_uid(requestDTO.getMerchant_uid());
        payment.setName(requestDTO.getName());
        payment.setAmount(requestDTO.getAmount());
        payment.setBuyer_email(requestDTO.getBuyer_email());
        payment.setBuyer_name(requestDTO.getBuyer_name());
        payment.setBuyer_tel(requestDTO.getBuyer_tel());
        payment.setBuyer_addr(requestDTO.getBuyer_addr());
        payment.setBuyer_postcode(requestDTO.getBuyer_postcode());

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment savedPayment = paymentService.savePayment(requestDTO);

        assertEquals(requestDTO.getImp_uid(), savedPayment.getImp_uid());
        assertEquals(requestDTO.getMerchant_uid(), savedPayment.getMerchant_uid());
        assertEquals(requestDTO.getName(), savedPayment.getName());
        assertEquals(requestDTO.getAmount(), savedPayment.getAmount());
        assertEquals(requestDTO.getBuyer_email(), savedPayment.getBuyer_email());
        assertEquals(requestDTO.getBuyer_name(), savedPayment.getBuyer_name());
        assertEquals(requestDTO.getBuyer_tel(), savedPayment.getBuyer_tel());
        assertEquals(requestDTO.getBuyer_addr(), savedPayment.getBuyer_addr());
        assertEquals(requestDTO.getBuyer_postcode(), savedPayment.getBuyer_postcode());
    }

    @Test
    public void testVerifyPayment() throws Exception {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO();
        requestDTO.setImp_uid("imp_12345678");
        requestDTO.setAmount(10000);

        String accessToken = "fake_access_token";

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("imp_key", apiKey);
        bodyMap.put("imp_secret", apiSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(bodyMap), headers);

        ResponseEntity<String> tokenResponse = ResponseEntity.ok("{\"response\":{\"access_token\":\"fake_access_token\"}}");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), eq(entity), eq(String.class))).thenReturn(tokenResponse);

        HttpHeaders paymentHeaders = new HttpHeaders();
        paymentHeaders.set("Authorization", accessToken);

        HttpEntity<String> paymentEntity = new HttpEntity<>(paymentHeaders);
        ResponseEntity<String> paymentResponse = ResponseEntity.ok("{\"response\":{\"amount\":10000,\"status\":\"paid\"}}");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(paymentEntity), eq(String.class))).thenReturn(paymentResponse);

        PaymentResponseDTO responseDTO = paymentService.verifyPayment(requestDTO);

        assertEquals(true, responseDTO.isSuccess());
        assertEquals("결제가 성공적으로 완료되었습니다.", responseDTO.getMessage());
    }
}
