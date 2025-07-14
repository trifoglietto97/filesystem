package com.example.filesystem.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("local")
public class LocalStorageService implements StorageService {

    private static final String BASE_PATH = "/app/data/";



    @Override
    public InputStream readFileStream(String path) throws Exception {
        Path resolvedPath = resolveSafePath(path);
        if (Files.notExists(resolvedPath)) {
            throw new IllegalArgumentException("File non trovato (local): " + path);
        }
        return Files.newInputStream(resolvedPath, StandardOpenOption.READ);
    }

    @Override
    public void writeFileBytes(String path, byte[] content) throws Exception {
        Path resolvedPath = resolveSafePath(path);
        System.out.println("Scrivendo file (bytes) a: " + resolvedPath.toAbsolutePath());
        Files.createDirectories(resolvedPath.getParent());
        Files.write(resolvedPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }


    @Override
    public List<String> listFiles(String directory, boolean recursive) throws IOException {
        Path basePath = Path.of(BASE_PATH);
        Path dirPath = resolveSafePath(directory);

        if (Files.notExists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Directory non trovata (local): " + directory);
        }

        List<String> fileList;

        if (recursive) {
            try (Stream<Path> stream = Files.walk(dirPath)) {
                fileList = stream
                        .filter(Files::isRegularFile)
                        .map(path -> basePath.relativize(path).toString())
                        .collect(Collectors.toList());
            }
        } else {
            try (Stream<Path> stream = Files.list(dirPath)) {
                fileList = stream
                        .filter(Files::isRegularFile)
                        .map(path -> basePath.relativize(path).toString())
                        .collect(Collectors.toList());
            }
        }

        return fileList;
    }

    @Override
    public void deleteFile(String path) throws IOException {
        Path resolvedPath = resolveSafePath(path);
        if (Files.exists(resolvedPath) && Files.isRegularFile(resolvedPath)) {
            Files.delete(resolvedPath);
        } else {
            throw new IllegalArgumentException("File non trovato o non è un file (local): " + path);
        }
    }

    @Override
    public long getFileSize(String path) throws Exception {
        Path resolvedPath = resolveSafePath(path);
        if (Files.notExists(resolvedPath)) {
            throw new IllegalArgumentException("File non trovato: " + path);
        }
        return Files.size(resolvedPath);
    }


     // Risolve il percorso relativo all’interno di BASE_PATH e previene path traversal.
    private Path resolveSafePath(String relativePath) {
        Path base = Path.of(BASE_PATH).toAbsolutePath().normalize();
        Path resolved = base.resolve(relativePath).normalize();

        if (!resolved.startsWith(base)) {
            throw new SecurityException("Tentativo di path traversal rilevato: " + relativePath);
        }
        return resolved;
    }
}
