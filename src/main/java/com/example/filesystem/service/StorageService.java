package com.example.filesystem.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface StorageService {



    List<String> listFiles(String path, boolean recursive) throws IOException;

    void deleteFile(String path) throws IOException;

    InputStream readFileStream(String path) throws Exception;

    void writeFileBytes(String path, byte[] content) throws Exception;

    long getFileSize(String path) throws Exception;

}
