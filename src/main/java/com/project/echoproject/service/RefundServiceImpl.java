package com.project.echoproject.service;

import org.springframework.stereotype.Service;

@Service
public class RefundServiceImpl implements RefundService {

    @Override
    public String getToken(String apiKey, String secretKey) {
        return null;
    }

    @Override
    public void refundWithToken(String token, String orderNumber, String message) {

    }
}
