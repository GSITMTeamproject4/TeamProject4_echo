package com.project.echoproject.service;

import com.project.echoproject.dto.order.CartItemDTO;
import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.CartItem;

public interface CartService {
    Cart addItemToCart(String userId, CartItemDTO cartItemDTO);
    Cart getCart(String userId);
    CartItem updateItemQuantity(Long cartItemId, int quantity);
    void removeItemFromCart(Long cartItemId);
}
