package com.project.echoproject.service;

import com.project.echoproject.entity.Image;
import com.project.echoproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public Image getOrCreateDefaultImage() {
        return imageRepository.findByFileName("default-profile.png")
                .orElseGet(() -> {
                    Image newImage = new Image();
                    newImage.setFileName("default-profile.png");
                    newImage.setFilePath("/img/default-profile.png");
                    newImage.setFileType("image/png");
                    newImage.setUploadTime(LocalDateTime.now());
                    newImage.setDefaultImage(true);  // 기본 이미지 플래그 설정
                    return imageRepository.save(newImage);
                });
    }


    @Override
    public Image saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        String rootPath = new File("").getAbsolutePath();
        String uploadPath = rootPath + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        logger.info("Upload directory: " + uploadPath);

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        file.transferTo(dest);
        logger.info("File saved to: " + dest.getAbsolutePath());

        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setFilePath(fileName);
        image.setUploadTime(LocalDateTime.now());
        image.setDefaultImage(false);  // 사용자 업로드 이미지는 기본 이미지가 아님
        return imageRepository.save(image);
    }

    @Override
    public String encodeImageToBase64(String filePath) throws IOException {
        String rootPath = new File("").getAbsolutePath();
        String fullPath = rootPath + File.separator + UPLOAD_DIR + filePath;
        byte[] imageData = java.nio.file.Files.readAllBytes(new File(fullPath).toPath());
        return Base64.getEncoder().encodeToString(imageData);
    }

    @Override
    public void deleteImage(Image image) {
        if (image != null) {
            // 파일 시스템에서 이미지 파일 삭제
            try {
                Files.deleteIfExists(Paths.get(image.getFilePath()));
            } catch (IOException e) {
                // 로그 기록 또는 예외 처리
            }

            // 데이터베이스에서 이미지 엔티티 삭제
            imageRepository.delete(image);
        }
    }

    @Override
    public Image cloneImageForUser(Image originalImage) {
        if (originalImage.isDefaultImage()) {
            return originalImage;  // 기본 이미지는 복제하지 않고 그대로 사용
        }

        Image clonedImage = new Image();
        clonedImage.setFileName(originalImage.getFileName());
        clonedImage.setFileType(originalImage.getFileType());
        clonedImage.setFilePath(originalImage.getFilePath());
        clonedImage.setUploadTime(LocalDateTime.now());
        clonedImage.setDefaultImage(false);
        return imageRepository.save(clonedImage);
    }
}
