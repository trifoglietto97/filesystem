package com.example.filesystem.controller;


import com.example.filesystem.service.StorageServiceSwitcher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {

    private final StorageServiceSwitcher storageServiceSwitcher;

    public FileController(StorageServiceSwitcher storageServiceSwitcher) {
        this.storageServiceSwitcher = storageServiceSwitcher;
    }

    @PostMapping("/write")
    public String writeFile(
            @RequestParam String filename,
            @RequestParam String content,
            @RequestParam String storage
    ) throws Exception {
        storageServiceSwitcher.writeFile(storage, filename, content);
        return "File salvato su " + storage;
    }

    @GetMapping("/read")
    public String readFile(
            @RequestParam String filename,
            @RequestParam String storage
    ) throws Exception {
        return storageServiceSwitcher.readFile(storage, filename);
    }
}

