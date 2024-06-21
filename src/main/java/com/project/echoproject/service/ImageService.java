package com.project.echoproject.service;

import com.project.echoproject.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image saveImage(MultipartFile file) throws IOException;
    String encodeImageToBase64(String filePath) throws IOException;
}
