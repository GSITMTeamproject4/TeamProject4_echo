package com.project.echoproject.service;

import com.project.echoproject.dto.CartItemDTO;
import com.project.echoproject.entity.Cart;

public interface CartService {
    Cart addItemToCart(String userId, CartItemDTO cartItemDTO);
    Cart getCart(String userId);
}
