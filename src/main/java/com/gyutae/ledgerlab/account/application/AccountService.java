package com.gyutae.ledgerlab.account.application;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.gyutae.ledgerlab.account.domain.Account;

//application: 계좌 생성·조회 같은 업무 흐름
//실제 api에서는 사용자가 만들지않고 서비스가 자동으로 ID를 생성함
@Service // 이걸붙이면 new AccountService() 안해도 스프링이 알아서 만들어줌
public class AccountService {
	private final AtomicLong idGenerator = new AtomicLong(1);
	private final AccountRepository accounts; // accountrepository 타입으로 필드를 둚. final: 이 필드는 생성자에서 초기화되면 바꾸지 않음

	public AccountService(AccountRepository accounts) { // 생성자에서 accountrepository를 주입받음. 매개변수로 accountrepository타입을 받아야
														// 함. 스프링이 알아서 구현체를 찾아서 주입해줌
		this.accounts = accounts; // 이렇게 생성자 주입을하면 @repo가 붙은 메모리구현체를 만들고 @service가 붙은 서비스에 주입해줌
	}

	public Account createAccount(BigDecimal initialBalance) {
		Long id = idGenerator.getAndIncrement();
		Account account = new Account(id, initialBalance);
		accounts.save(account); // 계좌를 저장소에 저장
		return account;
	}

	public Account findAccount(Long id) {
		return accounts.findById(id).orElseThrow(() -> new AccountNotFoundException(id)); // 계좌가 없으면 예외를 던짐 ()는 람다식
																							// 매개변수가 0개이고 ->는 이동작을 실행하면
																							// 오른쪽결과를 제공한다
		// 계좌가 없으면 예외를 던짐
	}

	public Account deposit(Long id, BigDecimal amount) {
		Account account = findAccount(id);
		account.deposit(amount);
		return account;
	}

	public Account withdraw(Long id, BigDecimal amount) {
		Account account = findAccount(id);
		account.withdraw(amount);
		return account;
	}

	public TransferResult transfer(
			Long fromAccountId,
			Long toAccountId,
			BigDecimal amount) { // 이렇게 매개변수는 타입과 변수선언과 같다 메서드를 밑에서 호출할때 구체적인 값이 들어온다
		if (fromAccountId.equals(toAccountId)) {
			throw new IllegalArgumentException("같은 계좌로 이체할 수 없습니다.");
		}
		Account fromAccount = findAccount(fromAccountId);
		Account toAccount = findAccount(toAccountId);

		fromAccount.withdraw(amount);
		toAccount.deposit(amount);

		// 서비스는 http를 몰라도되는 내부결과인 tranferresult를 반환함 request는 외부에서 들어오는값 result는 서비스가
		// 처리한 내부결과 response는 외부로 내보낼 값
		// 자바는 하나만 반환할수있으므로 fromaccouint, toaccount 두개의 계좌를 반환하기 위해 하나의 결과 객체로 묶음
		return new TransferResult(
				fromAccount,
				toAccount);
	}
}
