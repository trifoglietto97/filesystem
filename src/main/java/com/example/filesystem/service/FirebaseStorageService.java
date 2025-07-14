package com.example.filesystem.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service("firebase")
public class FirebaseStorageService implements StorageService {


    @Override
    public long getFileSize(String path) throws Exception {
        Bucket bucket = getBucket();
        Blob blob = bucket.get(path);
        if (blob == null) {
            throw new IllegalArgumentException("File non trovato in Firebase: " + path);
        }
        return blob.getSize();
    }


    @Override
    public InputStream readFileStream(String path){
        Bucket bucket = getBucket();
        Blob blob = bucket.get(path);
        if (blob == null) {
            throw new IllegalArgumentException("File non trovato: " + path);
        }
        return new ByteArrayInputStream(blob.getContent());
    }


    //TODO: qua un po di pezze eleganti, poi dimmi
    @Override
    public List<String> listFiles(String path, boolean recursive) {
        Bucket bucket = getBucket();
        List<String> fileList = new ArrayList<>();
        Iterable<Blob> blobs;

        if (recursive) {
            blobs = bucket.list(
                    Storage.BlobListOption.prefix(path)
            ).iterateAll();
        } else {
            blobs = bucket.list(
                    Storage.BlobListOption.prefix(path),
                    Storage.BlobListOption.delimiter("/")
            ).iterateAll();
        }

        for (Blob blob : blobs) {
            if (!blob.isDirectory()) {
                fileList.add(blob.getName());
            }
        }

        return fileList;
    }

    @Override
    public void deleteFile(String path) {
        Bucket bucket = getBucket();
        Blob blob = bucket.get(path);
        if (blob == null || !blob.delete()) {
            throw new IllegalArgumentException("File non trovato o impossibile eliminarlo da Firebase: " + path);
        }
    }


    @Override
    public void writeFileBytes(String path, byte[] content) throws Exception {
        Bucket bucket = getBucket();
        bucket.create(path, content);
    }

    private Bucket getBucket() {
        Bucket bucket = StorageClient.getInstance().bucket();
        if (bucket == null) {
            throw new IllegalStateException("Bucket Firebase non inizializzato");
        }
        return bucket;
    }
}


