package com.ncrde.church.integration.storage;

public interface StorageProvider {
    String storeFile(String key, byte[] fileData, String contentType);
    byte[] retrieveFile(String key);
    void deleteFile(String key);
}
