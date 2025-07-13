package com.example.filesystem.service;


import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service("firebase")
public class FirebaseStorageService implements StorageService {

    @Override
    public void writeFile(String filename, String content) throws Exception {
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create(filename, content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String readFile(String filename) throws Exception {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(filename);
        if (blob == null) {
            return "File non trovato (firebase)";
        }
        return new String(blob.getContent());
    }
}

