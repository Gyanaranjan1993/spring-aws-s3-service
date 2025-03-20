package com.spring.amazon.storage.s3storageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class S3StorageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3StorageServiceApplication.class, args);
	}

}
