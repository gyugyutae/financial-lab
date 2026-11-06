package com.gyutae.ledgerlab;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gyutae.ledgerlab.account.application.JpaAccountRepository;
import com.gyutae.ledgerlab.account.domain.Account;

@SpringBootTest
class JpaAccountRepositoryTest {

    @Autowired // jpaAccountRepository를 스프링이 관리하는 빈으로 등록하고 주입해준다
    private JpaAccountRepository jpaAccountRepository;

    @Test
    void testJpaAccountRepository() {
        Account account = new Account(new BigDecimal("10000"));
        jpaAccountRepository.save(account);
        assertThat(account.getId()).isNotNull();
    }
}