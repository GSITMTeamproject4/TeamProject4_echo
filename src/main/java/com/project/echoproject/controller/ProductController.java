package com.project.echoproject.controller;

import com.project.echoproject.entity.Product;
import com.project.echoproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/product")
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public String productHome(Model model, @RequestParam(defaultValue = "0") int page) {
        int size = 9; // 한 페이지에 보여줄 제품 수
        Page<Product> productPage = productService.getProductPage(page, size);

        model.addAttribute("productList", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "order/product";
    }

    // 제품 상세 페이지로 이동하는 메서드 추가
    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        try {
            Product product = this.productService.getProduct(id);
            model.addAttribute("product", product);
            return "order/product_detail";
        } catch (IllegalArgumentException e) {
            // 제품을 찾지 못한 경우 에러 페이지로 리다이렉트
            return "redirect:/error";
        }
    }
}
