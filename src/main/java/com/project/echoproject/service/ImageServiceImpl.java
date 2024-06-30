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
    public Image saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // 프로젝트 루트 디렉토리의 절대 경로를 가져옴
        String rootPath = new File("").getAbsolutePath();
        String uploadPath = rootPath + File.separator + UPLOAD_DIR;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // uploads 폴더가 존재하지 않으면 생성
        }

        logger.info("Upload directory: " + uploadPath);

        // 파일 저장 경로 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        file.transferTo(dest);

        logger.info("File saved to: " + dest.getAbsolutePath());

        // 이미지 엔티티 생성 및 저장
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setFilePath(fileName); // 파일 이름만 저장
        image.setUploadTime(LocalDateTime.now());

        return imageRepository.save(image); // 이미지 저장 후 반환
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
}
