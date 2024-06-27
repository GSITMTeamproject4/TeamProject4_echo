package com.project.echoproject.service;

public interface RefundService {
    String getToken(String apiKey, String secretKey);
    void refundWithToken(String token, String orderNumber, String message);
}
