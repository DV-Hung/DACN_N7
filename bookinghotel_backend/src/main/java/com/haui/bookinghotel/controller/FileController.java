package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.response.file.ResUploadFileDTO;
import com.haui.bookinghotel.service.FileService;
import com.haui.bookinghotel.util.annotation.ApiMessage;
import com.haui.bookinghotel.util.error.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${DACN.upload-file.base-uri}")
    private String baseUri;

    @PostMapping("/files")
    @ApiMessage("upload a file")
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        // validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please choose a file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "png", "doc", "docx", "jpeg");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("File is not valid. Please choose a valid extension");
        }
        // create a dir if not exist
        this.fileService.createUploadFolder(baseUri + folder);

        // store file
        String finalName = this.fileService.store(file, folder);
        ResUploadFileDTO res = new ResUploadFileDTO();
        res.setFileName(finalName);
        res.setUploadAt(Instant.now());

        return ResponseEntity.ok().body(res);
    }
}
