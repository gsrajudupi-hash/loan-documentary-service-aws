package com.ltm.training.loansdemo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ltm.training.loansdemo.entity.LoanApplication;
import com.ltm.training.loansdemo.service.LoanApplicationService;
import com.ltm.training.loansdemo.service.S3Service;

@RestController
@RequestMapping("/api")
public class LoanApplicationController {
	
	private final LoanApplicationService loanApplicationService;
	private final S3Service s3Service;
	
	public LoanApplicationController(LoanApplicationService loanApplicationService, S3Service s3Service) {
		super();
		this.loanApplicationService = loanApplicationService;
		this.s3Service = s3Service;
	}
	
	   // List S3 buckets
    @GetMapping("/buckets")
    public List<String> getBuckets() {

        return s3Service.listBuckets();
    }

    // Submit loan application
    @PostMapping(
        value = "/applications",
        consumes = "multipart/form-data"
    )
    public ResponseEntity<LoanApplication> submit(
            @RequestPart("application")
            LoanApplication application,
            @RequestPart("files")
            List<MultipartFile> files)
            throws IOException {

        LoanApplication saved = loanApplicationService.submitApplication(application, files);
        return ResponseEntity.ok(saved);
    }

    // Display applications

    @GetMapping("/applications")
    public List<LoanApplication> getApplications() {

        return loanApplicationService.getAllApplications();
    }

}
