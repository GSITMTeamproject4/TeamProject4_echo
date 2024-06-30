package com.project.echoproject.service;

import com.project.echoproject.dto.order.OrderDTO;
import com.project.echoproject.entity.Orders;
import com.project.echoproject.entity.SiteUser;

public interface OrderService {
    OrderDTO getOrderDetails(String orderNumber);
    Orders createOrder(String orderNumber, String buyerTel, String buyerEmail, String buyerAddr, String buyerPostcode, int totalAmount, String buyerId);
    void clearCart(SiteUser siteUser);
    Orders getOrderByOrderNumber(String orderNumber);
}
