package com.project.echoproject.service;

import com.project.echoproject.dto.OrderItemSaveDTO;
import com.project.echoproject.dto.OrderSaveDTO;
import com.project.echoproject.entity.Order;
import com.project.echoproject.entity.OrderItem;
import com.project.echoproject.entity.Product;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.OrderItemRepository;
import com.project.echoproject.repository.OrderRepository;
import com.project.echoproject.repository.ProductRepository;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final SiteUserRepository siteUserRepository;

    @Override
    public OrderSaveDTO getOrderDetails(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if (order == null) {
            return null;
        }

        OrderSaveDTO orderSaveDTO = new OrderSaveDTO();
        orderSaveDTO.setOrderNumber(order.getOrderNumber());
        orderSaveDTO.setBuyerTel(order.getBuyerTel());
        orderSaveDTO.setBuyerAddr(order.getBuyerAddr());
        orderSaveDTO.setBuyerPostcode(order.getBuyerPostcode());
        orderSaveDTO.setTotalAmount(order.getTotalAmount());
        orderSaveDTO.setBuyerId(order.getBuyer().getUserId()); // 추가

        // OrderItemSaveDTO 리스트 생성 및 설정
        List<OrderItemSaveDTO> items = order.getItems().stream()
                .map(item -> {
                    OrderItemSaveDTO dto = new OrderItemSaveDTO();
                    dto.setProductId(item.getProduct().getId()); // Product 엔티티의 ID 참조
                    dto.setQuantity(item.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
        orderSaveDTO.setItems(items);

        return orderSaveDTO;
    }

    public void saveOrder(OrderSaveDTO orderSaveDTO) {
        Order order = new Order();
        order.setOrderNumber(orderSaveDTO.getOrderNumber());
        order.setBuyerTel(orderSaveDTO.getBuyerTel());
        order.setBuyerAddr(orderSaveDTO.getBuyerAddr());
        order.setBuyerPostcode(orderSaveDTO.getBuyerPostcode());
        order.setTotalAmount(orderSaveDTO.getTotalAmount());

        SiteUser buyer = siteUserRepository.findByUserId(orderSaveDTO.getBuyerId()).orElseThrow(() -> new RuntimeException("User not found"));
        order.setBuyer(buyer);

        List<OrderItem> items = orderSaveDTO.getItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        orderRepository.save(order);
    }
}
