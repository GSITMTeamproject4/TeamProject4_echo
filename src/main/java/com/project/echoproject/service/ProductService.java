package com.project.echoproject.service;

import com.project.echoproject.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getList();
    Product getProduct(Long id);
}
