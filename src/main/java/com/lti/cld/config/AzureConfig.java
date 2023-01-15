package com.lti.cld.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Configuration
public class AzureConfig {

	@Value("${azure.storage.blobContainerNameforImages}")
	private String containerName;
	
	@Value("${azure.storage.blobContainerConnStringforImages}")
	private String connString;
	
	@Bean
	public BlobContainerClient blobContainerClient() {
		BlobServiceClient blobServiceClient= new BlobServiceClientBuilder().connectionString(connString).buildClient();
		return blobServiceClient.getBlobContainerClient(containerName);
	}
}
