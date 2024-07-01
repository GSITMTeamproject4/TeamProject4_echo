package com.project.echoproject.controller;

import com.project.echoproject.dto.order.OrderDTO;
import com.project.echoproject.entity.*;
import com.project.echoproject.repository.CartRepository;
import com.project.echoproject.repository.SiteUserRepository;
import com.project.echoproject.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final SiteUserRepository siteUserRepository;
    private final CartRepository cartRepository;
    private final OrderService orderService;

    public OrderController(SiteUserRepository siteUserRepository, CartRepository cartRepository, OrderService orderService) {
        this.siteUserRepository = siteUserRepository;
        this.cartRepository = cartRepository;
        this.orderService = orderService;
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

        List<String> productNames = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        int totalAmount = 0;
        for (CartItem item : cartItems) {
            productNames.add(item.getProduct().getProductName());
            quantities.add(item.getQuantity());
            productIds.add(item.getProduct().getId());
            int price = item.getProduct().getPrice();
            prices.add(price);
            totalAmount += price * item.getQuantity();
        }

        model.addAttribute("productIds", productIds);
        model.addAttribute("productNames", productNames);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("buyerEmail", siteUser.getEmail());
        model.addAttribute("buyerId", siteUser.getUserId());
        model.addAttribute("buyerTel", siteUser.getPhoneNum());
        model.addAttribute("buyerAddr", siteUser.getStreetaddr());
        model.addAttribute("buyerPostcode", siteUser.getZipcode());

        return "order/payment";
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> processOrder(@PathVariable String userId, @RequestBody Map<String, Object> orderData) {
        try {
            String orderNumber = (String) orderData.get("orderNumber");
            String buyerTel = (String) orderData.get("buyerTel");
            String buyerEmail = (String) orderData.get("buyerEmail");
            String buyerAddr = (String) orderData.get("buyerAddr");
            String buyerPostcode = (String) orderData.get("buyerPostcode");
            int totalAmount = (Integer) orderData.get("totalAmount");

            Orders createdOrder = orderService.createOrder(orderNumber, buyerTel, buyerEmail, buyerAddr, buyerPostcode, totalAmount, userId);

            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order processing failed: " + e.getMessage());
        }
    }

    @GetMapping("/success/{orderNumber}")
    public String orderSuccess(@PathVariable String orderNumber, Model model) {
        OrderDTO orderDTO = orderService.getOrderDetails(orderNumber);
        if (orderDTO == null) {
            return "redirect:/error";
        }
        model.addAttribute("order", orderDTO);
        return "order/order_success";
    }
}