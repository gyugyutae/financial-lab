package com.gyutae.financiallab.account.application;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gyutae.financiallab.account.domain.Account;

public interface JpaAccountRepository extends JpaRepository<Account, Long> {

}