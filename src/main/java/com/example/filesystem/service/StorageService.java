package com.example.filesystem.service;


public interface StorageService {
    void writeFile(String filename, String content) throws Exception;
    String readFile(String filename) throws Exception;
}

