package com.ltm.training.loansdemo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ltm.training.loansdemo.entity.LoanApplication;
import com.ltm.training.loansdemo.entity.LoanDocument;
import com.ltm.training.loansdemo.repository.LoanApplicationRepository;

@Service
public class LoanApplicationService {

	private final LoanApplicationRepository loanApplicationRepository;
	private final S3Service s3Service;

	public LoanApplicationService(LoanApplicationRepository loanApplicationRepository, S3Service s3Service) {
		super();
		this.loanApplicationRepository = loanApplicationRepository;
		this.s3Service = s3Service;
	}

	@Transactional
	public LoanApplication submitApplication(
			LoanApplication application,
			List<MultipartFile> files)
					throws IOException {

		List<String> uploadedKeys =new ArrayList<>();

		try {
			// Save application first to generate ID
			application = loanApplicationRepository.saveAndFlush(application);

			// Upload files to S3
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}
				String key = s3Service.uploadFile(file,application.getId());
				uploadedKeys.add(key);

				LoanDocument document =new LoanDocument();

				document.setOriginalFilename(file.getOriginalFilename());
				document.setS3Bucket(s3Service.getBucketName());
				document.setS3Key(key);
				document.setContentType(file.getContentType());
				application.addDocument(document);
			}

			return loanApplicationRepository.save(application);

		} catch (Exception ex) {

			// Compensate for uploaded S3 objects
			// if database processing fails.

			for (String key : uploadedKeys) {
				try {
					s3Service.deleteFile(key);
				} catch (Exception cleanupException) {
					ex.addSuppressed(cleanupException);
				}
			}

			throw new IllegalStateException("Loan application submission failed",ex);
		}
	}

	public List<LoanApplication> getAllApplications() {
		return loanApplicationRepository.findAll();
	}

}
