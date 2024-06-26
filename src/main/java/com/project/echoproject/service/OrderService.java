package com.project.echoproject.service;

import com.project.echoproject.dto.OrderSaveDTO;

import java.util.List;

public interface OrderService {
    void createOrder(OrderSaveDTO orderSaveDTO);
}
