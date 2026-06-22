package com.ncrde.church.integration.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalStorageProvider implements StorageProvider {

    private static final Logger logger = LoggerFactory.getLogger(LocalStorageProvider.class);
    private final Path rootDir = Paths.get("uploads").toAbsolutePath().normalize();

    public LocalStorageProvider() {
        try {
            if (!Files.exists(rootDir)) {
                Files.createDirectories(rootDir);
                logger.info("Created local storage uploads directory at: {}", rootDir);
            }
        } catch (IOException e) {
            logger.error("Could not initialize local storage uploads directory", e);
            throw new RuntimeException("Could not initialize local storage directory", e);
        }
    }

    @Override
    public String storeFile(String key, byte[] fileData, String contentType) {
        try {
            Path targetFile = rootDir.resolve(key).normalize();
            // Secure path validation against directory traversal attacks
            if (!targetFile.startsWith(rootDir)) {
                throw new SecurityException("Cannot store file outside target uploads directory");
            }
            // Ensure parent directories exist
            Files.createDirectories(targetFile.getParent());
            Files.write(targetFile, fileData);
            logger.info("Saved file locally at: {}", targetFile);
            return "/uploads/" + key;
        } catch (IOException e) {
            logger.error("Failed to store file locally with key: {}", key, e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public byte[] retrieveFile(String key) {
        try {
            Path targetFile = rootDir.resolve(key).normalize();
            if (!targetFile.startsWith(rootDir)) {
                throw new SecurityException("Cannot access file outside target uploads directory");
            }
            if (!Files.exists(targetFile)) {
                throw new RuntimeException("File not found: " + key);
            }
            return Files.readAllBytes(targetFile);
        } catch (IOException e) {
            logger.error("Failed to retrieve file with key: {}", key, e);
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            Path targetFile = rootDir.resolve(key).normalize();
            if (!targetFile.startsWith(rootDir)) {
                throw new SecurityException("Cannot delete file outside target uploads directory");
            }
            if (Files.exists(targetFile)) {
                Files.delete(targetFile);
                logger.info("Deleted local file: {}", targetFile);
            }
        } catch (IOException e) {
            logger.error("Failed to delete local file with key: {}", key, e);
        }
    }
}
