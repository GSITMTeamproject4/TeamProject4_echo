package com.project.echoproject.service;

import com.project.echoproject.dto.order.BuyerDTO;
import com.project.echoproject.dto.order.ItemDTO;
import com.project.echoproject.dto.order.OrderDTO;
import com.project.echoproject.entity.*;
import com.project.echoproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Override
    public Orders createOrder(String orderNumber, String buyerTel, String buyerEmail, String buyerAddr, String buyerPostcode, int totalAmount, String userId) {
        SiteUser siteUser = siteUserRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(siteUser).orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Orders orders = new Orders();
        orders.setOrderNumber(orderNumber);
        orders.setBuyer(siteUser);
        orders.setBuyerTel(buyerTel);
        orders.setBuyerEmail(buyerEmail);
        orders.setBuyerAddr(buyerAddr);
        orders.setBuyerPostcode(buyerPostcode);
        orders.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrders(orders);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        orders.setItems(orderItems);
        orderRepository.save(orders);


        return orders;
    }

    @Override
    public OrderDTO getOrderDetails(String orderNumber) {
        Orders orders = orderRepository.findByOrderNumber(orderNumber);

        if (orders == null) {
            throw new RuntimeException("Order not found for orderNumber: " + orderNumber);
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(orders.getOrderNumber());
        orderDTO.setTotalAmount(orders.getTotalAmount());

        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setUsername(orders.getBuyer().getUserId());
        buyerDTO.setBuyerTel(orders.getBuyerTel());  // 수정된 부분
        buyerDTO.setBuyerEmail(orders.getBuyerEmail());  // 수정된 부분
        buyerDTO.setBuyerAddr(orders.getBuyerAddr());  // 수정된 부분
        buyerDTO.setBuyerPostcode(orders.getBuyerPostcode());  // 수정된 부분
        orderDTO.setBuyer(buyerDTO);

        if (orders.getItems() != null) {
            List<ItemDTO> itemDTOList = orders.getItems().stream().map(item -> {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setProductName(item.getProduct().getProductName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getProduct().getPrice());
                return itemDTO;
            }).collect(Collectors.toList());
            orderDTO.setItems(itemDTOList);
        } else {
            orderDTO.setItems(Collections.emptyList());
        }

        return orderDTO;
    }

    @Transactional
    @Override
    public void clearCart(SiteUser siteUser) {
        Cart cart = cartRepository.findByUser(siteUser).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public Orders getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
}
