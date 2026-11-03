package com.ltm.training.loansdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ltm.training.loansdemo.entity.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

}
