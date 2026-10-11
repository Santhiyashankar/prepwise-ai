package com.prepwise.service.impl;

import com.prepwise.exception.ApiException;
import com.prepwise.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.public-url}")
    private String publicUrl;

    @Override
    public String storeAvatar(Long userId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ApiException("No file was uploaded", HttpStatus.BAD_REQUEST);
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ApiException(
                    "Only PNG, JPG and WEBP images are allowed",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ApiException(
                    "Image must be smaller than 5MB",
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Path avatarDir = Paths.get(uploadDir, "avatars");
            Files.createDirectories(avatarDir);

            String ext = resolveExtension(file.getOriginalFilename());

            String filename =
                    "user-" + userId + "-" + UUID.randomUUID() + ext;

            Path target = avatarDir.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return publicUrl + "/avatars/" + filename;

        } catch (IOException e) {
            throw new ApiException(
                    "Failed to store avatar image",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public StoredFile storeResume(Long userId,
                                  MultipartFile file,
                                  String extension) {

        if (file == null || file.isEmpty()) {
            throw new ApiException(
                    "Resume file is empty",
                    HttpStatus.BAD_REQUEST
            );
        }

        try {

            Path resumeDir = Paths.get(uploadDir, "resumes");
            Files.createDirectories(resumeDir);

            if (extension.startsWith(".")) {
                extension = extension.substring(1);
            }

            String filename =
                    "resume-" +
                            userId +
                            "-" +
                            UUID.randomUUID() +
                            "." +
                            extension;

            Path target = resumeDir.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            StoredFile storedFile = new StoredFile();
            storedFile.storedFileName = filename;
            storedFile.absolutePath = target.toAbsolutePath().toString();

            return storedFile;

        } catch (IOException e) {
            throw new ApiException(
                    "Failed to store resume",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private String resolveExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }

        return filename.substring(filename.lastIndexOf('.'));
    }
}