package com.example.filesystem.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class StorageServiceSwitcher {

    private final Map<String, StorageService> storageServices;

    public StorageServiceSwitcher(Map<String, StorageService> storageServices) {
        this.storageServices = storageServices;
    }

    public void writeFile(String storageType, String filename, String content) throws Exception {
        StorageService service = getStorageService(storageType);
        service.writeFile(filename, content);
    }

    public String readFile(String storageType, String filename) throws Exception {
        StorageService service = getStorageService(storageType);
        return service.readFile(filename);
    }

    private StorageService getStorageService(String storageType) {
        StorageService service = storageServices.get(storageType.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Storage type non supportato: " + storageType);
        }
        return service;

        
    }
}

