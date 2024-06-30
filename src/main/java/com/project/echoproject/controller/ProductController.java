package com.project.echoproject.controller;

import com.project.echoproject.entity.Product;
import com.project.echoproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/product")
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public String productHome(Model model) {
        List<Product> productList = this.productService.getList();
        model.addAttribute("productList", productList);
        return "product";
    }
}
