package com.project.echoproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.echoproject.dto.order.PaymentRequestDTO;
import com.project.echoproject.dto.order.PaymentResponseDTO;
import com.project.echoproject.entity.Payment;
import com.project.echoproject.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String apiSecret;

    // 결제 내역 저장
    @Override
    public Payment savePayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setImp_uid(paymentRequestDTO.getImp_uid());
        payment.setMerchant_uid(paymentRequestDTO.getMerchant_uid());
        payment.setName(paymentRequestDTO.getName());
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setBuyer_email(paymentRequestDTO.getBuyer_email());
        payment.setBuyer_name(paymentRequestDTO.getBuyer_name());
        payment.setBuyer_tel(paymentRequestDTO.getBuyer_tel());
        payment.setBuyer_addr(paymentRequestDTO.getBuyer_addr());
        payment.setBuyer_postcode(paymentRequestDTO.getBuyer_postcode());
        return paymentRepository.save(payment);
    }

    // 결제 검증
    @Override
    public PaymentResponseDTO verifyPayment(PaymentRequestDTO paymentRequestDTO) {
        try {
            String accessToken = getAccessToken();

            JSONObject paymentData = getPaymentData(paymentRequestDTO.getImp_uid(), accessToken);

            // 주문 정보와 결제 금액을 비교하는 로직을 여기서 작성
            if (paymentData.getInt("amount") == paymentRequestDTO.getAmount()) {
                switch (paymentData.getString("status")) {
                    case "ready":
                        // 가상 계좌가 발급된 상태
                        break;
                    case "paid":
                        // 결제가 완료된 상태
                        break;
                }
            } else {
                throw new IllegalArgumentException("결제 금액 검증에 실패하였습니다.");
            }

            return new PaymentResponseDTO(true, "결제가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return new PaymentResponseDTO(false, e.getMessage());
        }
    }

    @Override
    public String getAccessToken() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("imp_key", apiKey);
        bodyMap.put("imp_secret", apiSecret);

        ObjectMapper objectMapper = new ObjectMapper();
        String body;
        try {
            body = objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        try {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getJSONObject("response").getString("access_token");
        } catch (JSONException e) {
            throw new RuntimeException("JSON 파싱 오류", e);
        }
    }

    public JSONObject getPaymentData(String impUid, String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("결제 내역 조회 실패");
        }

        JSONObject responseBody = new JSONObject(response.getBody());
        return responseBody.getJSONObject("response");
    }
}
