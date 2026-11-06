package com.gyutae.ledgerlab.account.application;

/**
 * 계좌를 찾을 수 없는 경우 발생하는 예외
 * http 처리는 핸들러에 맞기고 예외는 예외 역할만 담당
 * 상속을하면 이클래스가 자바의 정식예외타입이된다
 */
public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(Long id) {
		super("계좌를 찾을 수 없습니다. id=" + id);
	}
}
