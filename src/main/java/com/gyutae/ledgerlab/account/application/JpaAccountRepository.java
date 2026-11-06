package com.gyutae.ledgerlab.account.application;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gyutae.ledgerlab.account.domain.Account;

public interface JpaAccountRepository extends JpaRepository<Account, Long> {

}