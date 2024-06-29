package com.project.echoproject.controller;

import com.project.echoproject.dto.order.CartItemDTO;
import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.CartService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final SiteUserService siteUserService;

    @PostMapping("/add")
    @ResponseBody
    public String addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("productId") Long productId,
                                @RequestParam(value = "quantity", defaultValue = "1") int quantity) {

        // 사용자 정보 가져오기
        String userId = userDetails.getUsername(); // 이제 이 부분이 userId를 반환합니다.
        SiteUser user = siteUserService.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // CartItemDTO 생성
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(productId);
        cartItemDTO.setQuantity(quantity);

        // 장바구니에 아이템 추가
        cartService.addItemToCart(user.getUserId(), cartItemDTO);

        // 성공 메시지 반환
        return "success";
    }

    @RequestMapping("/view")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 사용자 정보 가져오기
        String username = userDetails.getUsername();
        SiteUser user = siteUserService.findByUserId(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 장바구니 정보 가져오기
        Cart cart = cartService.getCart(user.getUserId());

        // 모델에 장바구니와 사용자 정보 추가
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);

        // 장바구니 페이지로 이동
        return "cart";
    }
}
