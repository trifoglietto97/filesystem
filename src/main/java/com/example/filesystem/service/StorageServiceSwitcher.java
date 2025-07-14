package com.example.filesystem.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class StorageServiceSwitcher {

    private final Map<String, StorageService> storageServices;

    public StorageServiceSwitcher(Map<String, StorageService> storageServices) {
        this.storageServices = storageServices;
    }

    public InputStream readFileStream(String storageType, String filename) throws Exception {
        return getStorageService(storageType).readFileStream(filename);
    }

    public List<String> listFiles(String storageType, String path, boolean recursive) throws IOException {
        return getStorageService(storageType).listFiles(path, recursive);
    }

    public void deleteFile(String storageType, String path) throws IOException {
        getStorageService(storageType).deleteFile(path);
    }

    private StorageService getStorageService(String storageType) {
        StorageService service = storageServices.get(storageType.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Storage type non supportato: " + storageType);
        }
        return service;
    }

    public void writeFileBytes(String storageType, String path, byte[] content) throws Exception {
        getStorageService(storageType).writeFileBytes(path, content);
    }

    public long getFileSize(String storageType, String path) throws Exception {
        return getStorageService(storageType).getFileSize(path);
    }


}
