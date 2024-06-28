package com.project.echoproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.echoproject.dto.order.OrderRequestDTO;
import com.project.echoproject.entity.Order;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import com.project.echoproject.service.OrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    private final IamportClient iamportClient;
    private final OrderService orderService;
    private final SiteUserRepository siteUserRepository;

    public OrderRestController(@Value("${imp.api.key}") String apiKey,
                               @Value("${imp.api.secretkey}") String apiSecret,
                               OrderService orderService,
                               SiteUserRepository siteUserRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.orderService = orderService;
        this.siteUserRepository = siteUserRepository;
    }

    @PostMapping("/verify/{imp_uid}")
    public ResponseEntity<IamportResponse<Payment>> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws Exception {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/success")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDTO orderRequestDTO,
                                                           Principal principal,
                                                           RedirectAttributes redirectAttributes) throws JsonProcessingException {

        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 사용자 응답
        }

        String userId = principal.getName();
        System.out.println("User ID: " + userId); // 디버깅용 로그
        Order order = orderService.createOrder(orderRequestDTO.getOrderNumber(),
                orderRequestDTO.getBuyerTel(),
                orderRequestDTO.getBuyerEmail(),
                orderRequestDTO.getBuyerAddr(),
                orderRequestDTO.getBuyerPostcode(),
                orderRequestDTO.getTotalAmount(),
                userId);

        // siteUser 객체 가져오기
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 주문 생성 후 장바구니 비우기
        orderService.clearCart(siteUser);

        // 주문 정보를 RedirectAttributes에 추가
        redirectAttributes.addFlashAttribute("order", order);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order created successfully");
        response.put("order", order);

        // 반환할 데이터를 로그로 출력하여 확인
        System.out.println("Response Data: " + new ObjectMapper().writeValueAsString(response)); // JSON 데이터 확인
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
