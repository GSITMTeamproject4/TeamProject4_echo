package com.project.echoproject.service;

import com.project.echoproject.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> getList();
    Product getProduct(Long id);
    Product addItem(String name, int price, MultipartFile file) throws IOException;
}
