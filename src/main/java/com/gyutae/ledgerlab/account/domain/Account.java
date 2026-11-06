package com.gyutae.ledgerlab.account.domain;
// balance는 입금하면 증가 출금하면 감소 잔액보다 많이 출금하면 InsufficientBalanceException 발생 0원보다작은 금액은 입출금할수없다.

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account { // 어카운트는 내부상태가 변경ㄱ되고 업무 규칙과 행동을 가지고 있으므로 레코드가아닌 일반클래스를 사용함
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal balance;

	protected Account() {
		// JPA를 위한 기본 생성자 매개변수를 비워둔다
	}

	public Account(BigDecimal balance) {
		this.balance = balance;
		if (balance == null) {
			throw new IllegalArgumentException("계좌 잔액이 null입니다.");
		}
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("계좌 잔액은 0원 이상이어야 합니다.");
		}
	}

	public Account(Long id, BigDecimal balance) { // 생성자 new로 객체를 만들대 실행된다 괄호안의 id, bnalance는 매개변수
		this.id = id;
		this.balance = balance; // 필드는 객체가 가지고있는 정부를 저장하는 변수 여기서는 필드가 id, balance를 저장함

		if (id == null) {
			throw new IllegalArgumentException("계좌 id가 null입니다.");
		}
		if (balance == null) {
			throw new IllegalArgumentException("계좌 잔액이 null입니다.");
		}
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("계좌 잔액은 0원 이상이어야 합니다.");
		}
	}

	public void deposit(BigDecimal amount) {

		if (amount == null) {
			throw new IllegalArgumentException("입금 금액이 null입니다.");
		}
		if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("입금 금액은 0원보다 커야 합니다.");
		}
		balance = balance.add(amount);
	} // amount가 0원보다 작으면 오류출력 0보다크면 balance에서 추가

	public void withdraw(BigDecimal amount) {
		if (amount == null) {
			throw new IllegalArgumentException("출금 금액이 null입니다.");
		}
		if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("출금 금액은 0원보다 커야 합니다.");
		}
		if (balance.compareTo(amount) < 0) {
			throw new InsufficientBalanceException();
		}
		balance = balance.subtract(amount);
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Long getId() {
		return id;
	}
}
