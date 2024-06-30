package com.project.echoproject.service;


import com.project.echoproject.dto.order.CartItemDTO;
import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.CartItem;
import com.project.echoproject.entity.Product;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.CartItemRepository;
import com.project.echoproject.repository.CartRepository;
import com.project.echoproject.repository.ProductRepository;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final SiteUserRepository siteUserRepository;

    @Override
    public Cart addItemToCart(String userId, CartItemDTO cartItemDTO) {
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(siteUser).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(siteUser);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 장바구니에 같은 제품이 있는지 확인
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            // 이미 장바구니에 있는 경우 수량 증가
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
        } else {
            // 장바구니에 없는 경우 새 항목 추가
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);

        return cart;
    }

    @Override
    public Cart getCart(String userId) {
        SiteUser user = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public CartItem updateItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);  // save 메서드는 저장된 엔티티를 반환합니다.
    }
    @Override
    @Transactional
    public void removeItemFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));
        Cart cart = cartItem.getCart();
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }
}

