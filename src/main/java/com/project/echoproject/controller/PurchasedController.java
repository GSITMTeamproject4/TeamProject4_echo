package com.project.echoproject.controller;

import com.project.echoproject.entity.Orders;
import com.project.echoproject.service.OrderService;
import com.project.echoproject.service.PurchasedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PurchasedController {

    private final PurchasedService purchasedService;

    @Autowired
    public PurchasedController(  PurchasedService purchasedService) {
        this.purchasedService = purchasedService;
    }

    @GetMapping("/order")
    public String showOrderList(Model model) {
        List<Orders> orders = purchasedService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderItem"; // order-list.html 템플릿을 반환
    }
}
