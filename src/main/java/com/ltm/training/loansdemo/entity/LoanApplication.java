package com.ltm.training.loansdemo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "loan_application")
public class LoanApplication {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	private String customerName;
	private String email;
	private String mobile;
	private String loanType;
	private BigDecimal loanAmount;
	private LocalDateTime applicationDate;

	@OneToMany(
			mappedBy = "application",
			cascade = CascadeType.ALL,
			orphanRemoval = true
			)
	private List<LoanDocument> documents =new ArrayList<>();

	@PrePersist
	public void onCreate() {
		applicationDate = LocalDateTime.now();
	}

	public void addDocument(LoanDocument document) {
		documents.add(document);
		document.setApplication(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public LocalDateTime getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(LocalDateTime applicationDate) {
		this.applicationDate = applicationDate;
	}

	public List<LoanDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<LoanDocument> documents) {
		this.documents = documents;
	}

	
}
