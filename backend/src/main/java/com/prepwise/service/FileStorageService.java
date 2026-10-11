package com.prepwise.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeAvatar(Long userId, MultipartFile file);

    StoredFile storeResume(Long userId, MultipartFile file, String extension);

    class StoredFile {
        public String storedFileName;
        public String absolutePath;
    }
}
