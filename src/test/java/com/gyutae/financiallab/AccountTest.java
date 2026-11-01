package com.gyutae.financiallab;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.gyutae.financiallab.account.application.AccountNotFoundException;
import com.gyutae.financiallab.account.application.AccountRepository;
import com.gyutae.financiallab.account.application.AccountService;
import com.gyutae.financiallab.account.application.InMemoryAccountRepository;
import com.gyutae.financiallab.account.application.TransferResult;
import com.gyutae.financiallab.account.domain.Account;
import com.gyutae.financiallab.account.domain.InsufficientBalanceException;

public class AccountTest {
	@Test
	void deposit() {
		// given
		Account account = new Account(1L, new BigDecimal("0"));
		// when
		account.deposit(new BigDecimal("3000"));
		// then
		assertThat(account.getBalance()).isEqualTo(new BigDecimal("3000"));
	}

	@Test
	void initialbalance() {
		// given
		Account account = new Account(1L, new BigDecimal("0"));
		// when
		BigDecimal balance = account.getBalance();
		// then
		assertThat(balance).isEqualTo(new BigDecimal("0"));
	}

	@Test
	void doubleDeposit() {
		Account account = new Account(1L, new BigDecimal("0"));
		account.deposit(new BigDecimal("3000"));
		account.deposit(new BigDecimal("5000"));
		assertThat(account.getBalance()).isEqualTo(new BigDecimal("8000"));
	}

	@Test
	void withdraw() {
		Account account = new Account(1L, new BigDecimal("0"));
		account.deposit(new BigDecimal("10000"));
		account.withdraw(new BigDecimal("3000"));
		assertThat(account.getBalance()).isEqualTo(new BigDecimal("7000"));
	}

	@Test
	void InsufficientBalanceException() {
		// given
		Account account = new Account(1L, new BigDecimal("0"));
		account.deposit(new BigDecimal("10000"));
		// when
		assertThatThrownBy(
				() -> account.withdraw(new BigDecimal("13000"))).isInstanceOf(InsufficientBalanceException.class);

		// then
		assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
	}

	@Test
	void withrawAllBalance() {
		Account account = new Account(1L, new BigDecimal("0"));
		account.deposit(new BigDecimal("10000"));
		account.withdraw(new BigDecimal("10000"));
		assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("0"));
	}

	@Test
	void depositZeroAmount() {
		Account account = new Account(1L, new BigDecimal("0"));
		assertThatThrownBy(
				() -> account.deposit(new BigDecimal("0"))).isInstanceOf(IllegalArgumentException.class);

		assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("0"));
	}

	@Test
	void getId() {
		Account account = new Account(1L, new BigDecimal("0"));
		assertThat(account.getId()).isEqualTo(1L);
	}

	@Test
	void IDNullException() {
		assertThatThrownBy(
				() -> new Account(null, new BigDecimal("0"))).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(
				() -> new Account(1L, null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void transfer() {
		// given
		AccountService accountService = new AccountService(new InMemoryAccountRepository()); // accountservice객체를 생성해야
																								// create transfer를
																								// 호출할수있음
		Account fromAccount = accountService.createAccount(new BigDecimal("10000"));
		Account toAccount = accountService.createAccount(new BigDecimal("5000"));
		TransferResult result = accountService.transfer(fromAccount.getId(), toAccount.getId(), new BigDecimal("3000")); // when
																															// //거래가
																															// 성공하므로
																															// transferresult를
																															// 반환함
		// then
		assertThat(result.fromAccount().getBalance()).isEqualByComparingTo(new BigDecimal("7000"));
		assertThat(result.toAccount().getBalance()).isEqualByComparingTo(new BigDecimal("8000")); // isEqualByComparingTo가
																									// BigDecimal 비교할때는
																									// 더적합. isEqualTo는
																									// 소수점 자릿수 scale까지도
																									// 비교함
		assertThat(result.fromAccount().getId()).isEqualTo(fromAccount.getId());
		assertThat(result.toAccount().getId()).isEqualTo(toAccount.getId());
	}

	@Test
	void transferInsufficientBalance() {
		AccountService accountService = new AccountService(new InMemoryAccountRepository());
		Account fromAccount = accountService.createAccount(new BigDecimal("10000"));
		Account toAccount = accountService.createAccount(new BigDecimal("5000"));
		// when
		assertThatThrownBy(
				() -> accountService.transfer(fromAccount.getId(), toAccount.getId(), new BigDecimal("13000")))
				.isInstanceOf(InsufficientBalanceException.class);
		assertThat(fromAccount.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
		assertThat(toAccount.getBalance()).isEqualByComparingTo(new BigDecimal("5000"));
	}

	@Test
	void transferNotFoundAccount() {
		AccountService accountService = new AccountService(new InMemoryAccountRepository());
		Account fromAccount = accountService.createAccount(new BigDecimal("27000"));
		// when
		assertThatThrownBy(
				() -> accountService.transfer(fromAccount.getId(), 999L, new BigDecimal("1000")))
				.isInstanceOf(AccountNotFoundException.class);
		assertThat(fromAccount.getBalance()).isEqualByComparingTo(new BigDecimal("27000"));
	}

	@Test
	void transferSameAccount() {
		AccountService accountService = new AccountService(new InMemoryAccountRepository());
		Account fromAccount = accountService.createAccount(new BigDecimal("10000"));
		// when
		assertThatThrownBy(
				() -> accountService.transfer(fromAccount.getId(), fromAccount.getId(), new BigDecimal("1000")))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(fromAccount.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
	}

	@Test
	void saveAndFindAccount() {
		AccountRepository accountRepository = new InMemoryAccountRepository();
		Account account = new Account(1L, new BigDecimal("10000"));
		accountRepository.save(account);

		Account retrievedAccount = accountRepository.findById(1L).orElseThrow(); // 계좌가 있으면 optional에서 account를 꺼내고 없으면
																					// 예외를 던짐
		assertThat(retrievedAccount.getBalance()).isEqualByComparingTo("10000");
		assertThat(retrievedAccount.getId()).isEqualTo(1L);
	}

	@Test
	void NotFoundAccount() {
		AccountRepository accountRepository = new InMemoryAccountRepository(); // 인메모리가 어카운트레포를 구현했으므로 인스턴스를 생성해서
		assertThat(accountRepository.findById(1L)).isEmpty();
	}

	@Test
	void createAccount() {
		Account account = new Account(new BigDecimal("10000"));
		assertThat(account.getId()).isNull();
		assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
	}
}
