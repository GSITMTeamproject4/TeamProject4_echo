package com.project.echoproject.service;

import com.project.echoproject.entity.Orders;
import com.project.echoproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PurchasedService {

    private final OrderRepository orderItemRepository;

    @Autowired
    public PurchasedService(OrderRepository orderRepository) {
        this.orderItemRepository = orderRepository;
    }

    public List<Orders> getAllOrders() {
        return orderItemRepository.findAll();
    }
}



