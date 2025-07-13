package com.example.filesystem.service;


import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;

@Service("local")
public class LocalStorageService implements StorageService {

    private static final String BASE_PATH = "/app/data/";

    @Override
    public void writeFile(String filename, String content) throws Exception {
        Path path = Path.of(BASE_PATH, filename);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }

    @Override
    public String readFile(String filename) throws Exception {
        Path path = Path.of(BASE_PATH, filename);
        if (Files.notExists(path)) {
            return "File non trovato (local)";
        }
        return Files.readString(path);
    }
}
