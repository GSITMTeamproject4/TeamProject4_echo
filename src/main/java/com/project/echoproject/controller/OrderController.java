package com.project.echoproject.controller;

import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.CartItem;
import com.project.echoproject.entity.Orders;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.CartRepository;
import com.project.echoproject.repository.SiteUserRepository;
import com.project.echoproject.service.CartService;
import com.project.echoproject.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final SiteUserRepository siteUserRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrderService orderService;

    public OrderController(SiteUserRepository siteUserRepository,
                           CartRepository cartRepository,
                           CartService cartService,
                           OrderService orderService) {
        this.siteUserRepository = siteUserRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
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

        return "payment";
    }


    @GetMapping("/success/{orderNumber}")
    public String orderSuccess(@PathVariable String orderNumber, Model model) {
        Orders order = orderService.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            return "redirect:/error";
        }
        model.addAttribute("order", order);
        return "order_success";
    }
}