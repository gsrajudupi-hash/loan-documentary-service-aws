package com.ltm.training.loansdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_document")
public class LoanDocument {
	
	 	@Id
	    @GeneratedValue(strategy =GenerationType.IDENTITY)
	    private Long id;
	    private String originalFilename;
	    private String s3Bucket;

	    @Column(length = 1000)
	    private String s3Key;
	    private String contentType;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "application_id")
	    private LoanApplication application;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getOriginalFilename() {
			return originalFilename;
		}

		public void setOriginalFilename(String originalFilename) {
			this.originalFilename = originalFilename;
		}

		public String getS3Bucket() {
			return s3Bucket;
		}

		public void setS3Bucket(String s3Bucket) {
			this.s3Bucket = s3Bucket;
		}

		public String getS3Key() {
			return s3Key;
		}

		public void setS3Key(String s3Key) {
			this.s3Key = s3Key;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public LoanApplication getApplication() {
			return application;
		}

		public void setApplication(LoanApplication application) {
			this.application = application;
		}
	    
	    

}
