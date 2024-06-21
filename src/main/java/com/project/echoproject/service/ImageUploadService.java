package com.project.echoproject.service;

import com.project.echoproject.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    Image saveImage(MultipartFile file) throws IOException;
    Image getImage(Long id);
}
