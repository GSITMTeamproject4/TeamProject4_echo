package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Coupon;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final ImageService imageService;

    public List<Coupon> getList() {
        return couponRepository.findAll();
    }

    public Coupon getCoupon(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if(coupon.isPresent()) {
            return coupon.get();
        }else {
            throw new IllegalArgumentException("Coupon not found");
        }
    }


    public Coupon addCoupon(String name, Long point, MultipartFile file) throws IOException {
        Coupon coupon = new Coupon();

        coupon.setCouponPoint(point);
        coupon.setCouponName(name);

        if (!file.isEmpty()) {
            // ImageService를 사용하여 이미지 저장
            Image image = imageService.saveImage(file);
            coupon.setImage(image);
            coupon.setCheckImg(image.getFilePath()); // checkImg 필드 설정
        } else {
            throw new IOException("파일 없음");
        }

        return couponRepository.save(coupon);
    }
}