package com.project.echoproject.service;

import com.project.echoproject.entity.Coupon;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.Product;
import com.project.echoproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Override
    public List<Product> getList() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            return  product.get();
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }

    public Product addItem(String name, int price, MultipartFile file) throws IOException {
        Product product = new Product();

        product.setProductName(name);
        product.setPrice(price);

        if (!file.isEmpty()) {
            // ImageService를 사용하여 이미지 저장
            Image image = imageService.saveImage(file);
            product.setProductImage(image);
            product.setCheckImg(image.getFilePath()); // checkImg 필드 설정
        }
        return productRepository.save(product);
    }
}
