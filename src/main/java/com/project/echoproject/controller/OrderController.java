package com.project.echoproject.controller;

import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.CartItem;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.CartRepository;
import com.project.echoproject.repository.ProductRepository;
import com.project.echoproject.repository.SiteUserRepository;
import com.project.echoproject.service.CartService;
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
@RequestMapping("/order")
public class OrderController {

    private final IamportClient iamportClient;
    private final SiteUserRepository siteUserRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    public OrderController(@Value("111") String apiKey,
                           @Value("111") String apiSecret,
                           SiteUserRepository siteUserRepository,
                           CartRepository cartRepository,
                           CartService cartService,

                           OrderService orderService,
                           ProductRepository productRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.siteUserRepository = siteUserRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
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

    @GetMapping("/checkout/{userId}")
    public String checkout(@PathVariable String userId, Model model) {
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(siteUser)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        int totalAmount = cartItems.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        List<String> productNames = cartItems.stream()
                .map(item -> item.getProduct().getProductName())
                .collect(Collectors.toList());

        // 모델에 데이터 추가
        model.addAttribute("productNames", productNames);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("buyerEmail", siteUser.getEmail());
        model.addAttribute("buyerName", siteUser.getUserId());
        model.addAttribute("buyerTel", siteUser.getPhoneNum());
        model.addAttribute("buyerAddr", siteUser.getAddress());
        model.addAttribute("buyerPostcode", siteUser.getAddress());

        return "payment";
    }
}
