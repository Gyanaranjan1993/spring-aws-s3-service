package com.spring.amazon.storage.s3storageservice;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class S3StorageController {

    private final S3StorageService s3StorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                             @RequestParam(value = "s3key") String s3key) {
        s3StorageService.uploadFile(file, s3key);
        return ResponseEntity.ok().body("Successfully uploaded file");
    }

    @PostMapping("/download")
    public ResponseEntity<String> downloadFile(@RequestBody DownloadPojo downloadPojo) {
        s3StorageService.downloadFile(downloadPojo.getS3Key(), downloadPojo.getFilePath());
        return ResponseEntity.ok().body("Successfully downloaded file");
    }

    @GetMapping("/getall")
    public ResponseEntity<List<String>> listAllFiles(@RequestParam(value = "s3key") String s3key) {
        List<String> s3Objects = s3StorageService.listAllObjects(s3key);
        return ResponseEntity.ok().body(s3Objects);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class DownloadPojo {
        String s3Key;
        String filePath;
    }
}
