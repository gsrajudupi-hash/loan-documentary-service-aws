package com.ltm.training.loansdemo.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;
/**
 * S3Service is a Spring service class that provides methods to interact with Amazon S3 
 * for file storage and retrieval. 
 * It uses the AWS SDK's S3Client to perform operations such as listing buckets, 
 * uploading files, and deleting files from S3.
 * 
 * The methods of S3Client are used to perform the actual S3 operations, 
 * Methods: 
 * 		listBuckets(), uploadFile(), deleteFile(), and getBucketName() 
 * 		provide a higher-level abstraction for interacting with S3.
 * 		putObject() method is used to upload files to S3, 
 * 		deleteObject() method is used to delete files from S3.
 * 		getObject() method is used to retrieve files from S3.
 */
@Service
public class S3Service {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	public S3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	// List all S3 buckets

	public List<String> listBuckets() {

		return s3Client.listBuckets()
				.buckets()
				.stream()
				.map(Bucket::name)
				.toList();
	}

	// Upload document to S3

	public String uploadFile(
			MultipartFile file,
			Long applicationId) throws IOException {

		String originalName =
				file.getOriginalFilename();

		String safeName = originalName == null
				? "document"
						: originalName.replaceAll(
								"[^a-zA-Z0-9._-]", "_");

		String objectKey =
				"applications/" + applicationId
				+ "/" + UUID.randomUUID()
				+ "-" + safeName;

		PutObjectRequest request =
				PutObjectRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.contentType(file.getContentType())
				.serverSideEncryption(
						ServerSideEncryption.AES256)
				.build();

		s3Client.putObject(
				request,
				RequestBody.fromInputStream(
						file.getInputStream(),
						file.getSize()
						)
				);

		return objectKey;
	}

	// Delete document from S3

	public void deleteFile(String objectKey) {

		DeleteObjectRequest request =
				DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.build();

		s3Client.deleteObject(request);
	}

	public String getBucketName() {
		return bucketName;
	}

}
