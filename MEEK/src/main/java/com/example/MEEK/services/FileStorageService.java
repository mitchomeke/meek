package com.example.MEEK.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path storageLocation;

    public FileStorageService(@Value("${file.upload-dir:uploads/avatars}") String uploadDir) {
        this.storageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }
    @PostConstruct
    public void init(){
        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create this directory",e);
        }
    }
    public String store(MultipartFile file) throws Exception {
        if (file.isEmpty() || file == null){
            throw new IllegalArgumentException("Cannot save an empty file.");
        }
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")){
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        Path destination = this.storageLocation.resolve(uniqueFileName).normalize();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream,destination, StandardCopyOption.REPLACE_EXISTING);
        }
        return uniqueFileName;
    }

}
