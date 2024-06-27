package com.project.echoproject.service;

import com.project.echoproject.dto.OrderSaveDTO;
import com.project.echoproject.dto.OrderItemSaveDTO;
import com.project.echoproject.entity.*;
import com.project.echoproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final SiteUserRepository siteUserRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createOrder(OrderSaveDTO orderSaveDTO) {
        SiteUser buyer = siteUserRepository.findById(orderSaveDTO.getBuyerId()).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setOrderNumber(orderSaveDTO.getOrderNumber());
        order.setBuyer(buyer);
        order.setBuyerTel(orderSaveDTO.getBuyerTel());
        order.setBuyerAddr(orderSaveDTO.getBuyerAddr());
        order.setBuyerPostcode(orderSaveDTO.getBuyerPostcode());
        order.setTotalAmount(orderSaveDTO.getTotalAmount());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemSaveDTO itemDTO : orderSaveDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        orderRepository.save(order);

        // 주문이 생성되었으므로 장바구니를 비웁니다.
        Cart cart = cartRepository.findByUser(buyer).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getItems());
    }
}
