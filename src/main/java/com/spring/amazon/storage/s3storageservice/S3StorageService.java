package com.spring.amazon.storage.s3storageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {

    private final S3Client s3Client;
    private final AWSClientConfig clientConfig;

    public void uploadFile(MultipartFile file, String s3key) {
        String fileName = System.currentTimeMillis() + "." + file.getOriginalFilename();
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(clientConfig.getBucketName())
                        .key(s3key)
                        .build(),
                RequestBody.fromFile(convertMultiPartToFile(file)));
        log.info("Uploaded file: " + fileName);
    }

    public void downloadFile(String s3key, String fileName) {
        s3Client.getObject(GetObjectRequest.builder()
                        .bucket(clientConfig.getBucketName())
                        .key(s3key)
                        .build(),
                ResponseTransformer.toFile(Paths.get(fileName)));
        log.info("File downloaded to {}", fileName);
    }

    public List<String> listAllObjects(String s3key) {
        List<String> s3ObjectsList = new ArrayList<>();
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(s3key)
                .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        listObjectsResponse.contents().forEach(s3Object ->
                s3ObjectsList.add(s3Object.key()));


        return s3ObjectsList;
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) {
        File convertedFile = new File(multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipart file to file", e);

        }
        return convertedFile;

    }
}
