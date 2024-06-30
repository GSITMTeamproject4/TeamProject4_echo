package com.project.echoproject.controller;

import com.project.echoproject.dto.order.CartItemDTO;
import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.CartItem;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.CartService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final SiteUserService siteUserService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addItemToCart(@RequestParam("productId") Long productId,
                                                @RequestParam(value = "quantity", defaultValue = "1") int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String userId = authentication.getName();
        SiteUser user = siteUserService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // CartItemDTO 생성
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(productId);
        cartItemDTO.setQuantity(quantity);

        // 장바구니에 아이템 추가
        cartService.addItemToCart(user.getUserId(), cartItemDTO);

        // 성공 메시지 반환
        return ResponseEntity.ok("success");
    }

    @GetMapping("/view")
    public String viewCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("loginRequired", true);
            model.addAttribute("cart", new Cart()); // 빈 카트 객체 추가
            return "order/cart";
        }

        // 장바구니 정보 가져오기
        String userId = authentication.getName();
        SiteUser user = siteUserService.findByUserId(userId);
        if (user == null) {
            model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            return "order/cart";
        }

        // 모델에 장바구니와 사용자 정보 추가
        try {
            Cart cart = cartService.getCart(user.getUserId());
            if (cart == null) {
                cart = new Cart(); // 빈 카트 객체 생성
            }
            int totalAmount = cart.getItems().stream()
                    .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("cart", cart);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", "장바구니를 불러오는 중 오류가 발생했습니다.");
        }

        // 장바구니 페이지로 이동
        return "order/cart";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateCartItemQuantity(@RequestParam Long cartItemId, @RequestParam int quantity) {
        try {
            CartItem updatedItem = cartService.updateItemQuantity(cartItemId, quantity);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "newQuantity", updatedItem.getQuantity(),
                            "newTotalPrice", updatedItem.getProduct().getPrice() * updatedItem.getQuantity()
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<?> removeCartItem(@RequestParam Long cartItemId) {
        try {
            cartService.removeItemFromCart(cartItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to remove item");
        }
    }
}
