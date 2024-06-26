package com.project.echoproject.controller;

import com.project.echoproject.dto.OrderSaveDTO;
import com.project.echoproject.entity.Product;
import com.project.echoproject.repository.ProductRepository;
import com.project.echoproject.service.OrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    private final IamportClient iamportClient;
    private final String apiKey;
    private final String apiSecret;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    public OrderController(@Value("${imp.api.key}") String apiKey,
                           @Value("${imp.api.secretkey}") String apiSecret,
                           OrderService orderService,
                           ProductRepository productRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.orderService = orderService;
        this.productRepository = productRepository;
    }

    @ResponseBody
    @PostMapping("/verify/{imp_uid}")
    public ResponseEntity<IamportResponse<Payment>> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws Exception {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/{orderNumber}")
    public String getOrderPage(@PathVariable("orderNumber") String orderNumber, Model model) {
        OrderSaveDTO order = orderService.getOrderDetails(orderNumber);
        if (order != null) {
            model.addAttribute("orderNumber", order.getOrderNumber());
            model.addAttribute("buyerTel", order.getBuyerTel());
            model.addAttribute("buyerAddr", order.getBuyerAddr());
            model.addAttribute("buyerPostcode", order.getBuyerPostcode());
            model.addAttribute("totalAmount", order.getTotalAmount());
            model.addAttribute("buyerId", order.getBuyerId());

            // Product 이름을 추가합니다.
            List<String> productNames = order.getItems().stream()
                    .map(item -> {
                        // ProductRepository를 통해 productName을 가져옵니다.
                        return productRepository.findById(item.getProductId())
                                .map(Product::getProductName)
                                .orElse("Unknown Product");
                    })
                    .collect(Collectors.toList());
            model.addAttribute("productNames", productNames);
        }
        return "payment";
    }
}
