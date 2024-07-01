package com.project.echoproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.echoproject.dto.order.OrderRequestDTO;
import com.project.echoproject.entity.Orders;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import com.project.echoproject.service.OrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final IamportClient iamportClient;
    private final OrderService orderService;
    private final SiteUserRepository siteUserRepository;
    private final ObjectMapper objectMapper; // ObjectMapper 필드 추가

    // 생성자 수정
    public OrderRestController(@Value("${imp.api.key}") String apiKey,
                               @Value("${imp.api.secretkey}") String apiSecret,
                               OrderService orderService,
                               SiteUserRepository siteUserRepository,
                               ObjectMapper objectMapper) { // ObjectMapper 주입
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.orderService = orderService;
        this.siteUserRepository = siteUserRepository;
        this.objectMapper = objectMapper; // 주입된 ObjectMapper 필드 초기화
    }

    @PostMapping("/verify/{imp_uid}")
    public ResponseEntity<IamportResponse<Payment>> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws Exception {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/success")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequestDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body("Unauthorized user");
        }
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = orderService.createOrder(orderRequestDTO.getOrderNumber(),
                orderRequestDTO.getBuyerTel(),
                orderRequestDTO.getBuyerEmail(),
                orderRequestDTO.getBuyerAddr(),
                orderRequestDTO.getBuyerPostcode(),
                orderRequestDTO.getTotalAmount(),
                userId);

        orderService.clearCart(siteUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order created successfully");
        response.put("orderNumber", order.getOrderNumber());

        return ResponseEntity.ok(response);
    }

}
