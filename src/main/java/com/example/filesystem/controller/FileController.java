package com.example.filesystem.controller;

import com.example.filesystem.dto.FileMetaDto;
import com.example.filesystem.service.StorageServiceSwitcher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private final StorageServiceSwitcher storageServiceSwitcher;

    public FileController(StorageServiceSwitcher storageServiceSwitcher) {
        this.storageServiceSwitcher = storageServiceSwitcher;
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("storage") String storage,
            @RequestParam(value = "path", required = false, defaultValue = "") String path
    ) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Il file deve avere un nome valido.");
        }

        byte[] content = file.getBytes();

        String fullPath = (path.isEmpty())
                ? filename
                : path.endsWith("/") ? path + filename : path + "/" + filename;

        storageServiceSwitcher.writeFileBytes(storage, fullPath, content);

        return "File \"" + fullPath + "\" salvato su " + storage;
    }


    @GetMapping("/get")
    public ResponseEntity<StreamingResponseBody> getFile(
            @RequestParam String storage,
            @RequestParam String path
    ) throws Exception {
        InputStream stream = storageServiceSwitcher.readFileStream(storage, path);

        StreamingResponseBody responseBody = outputStream -> {
            stream.transferTo(outputStream);
        };

        String filename = path.substring(path.lastIndexOf("/") + 1);

        // eventuale rilevamento MIME type
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(responseBody);
    }


    @GetMapping("/get-list")
    public List<FileMetaDto> listFileMetadata(
            @RequestParam String storage,
            @RequestParam String path,
            @RequestParam(defaultValue = "false") boolean recursive
    ) throws Exception {
        List<String> filePaths = storageServiceSwitcher.listFiles(storage, path, recursive);

        List<FileMetaDto> result = new ArrayList<>();
        for (String filePath : filePaths) {
            String filename = filePath.substring(filePath.lastIndexOf("/") + 1);

            long size = storageServiceSwitcher.getFileSize(storage, filePath); // nuovo metodo

            result.add(new FileMetaDto(filePath, filename, size));
        }
        return result;
    }


    @DeleteMapping("/delete")
    public String deleteFile(
            @RequestParam String storage,
            @RequestParam String path
    ) throws IOException {
        storageServiceSwitcher.deleteFile(storage, path);
        return "File \"" + path + "\" eliminato da " + storage;
    }
}
