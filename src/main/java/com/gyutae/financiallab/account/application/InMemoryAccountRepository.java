package com.gyutae.financiallab.account.application;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.gyutae.financiallab.account.domain.Account;

/**
 * 인메모리 계좌 저장소
 * 구현클래스여서 @Repository를 붙여서 스프링이 알아서 만들어줌
 */
@Repository // 이걸붙이면 new InMemoryAccountRepository() 안해도 스프링이 알아서 만들어줌
public class InMemoryAccountRepository implements AccountRepository { // 이 클래스가 레포 인터페이스의 약속을 구현한다
	private final Map<Long, Account> accounts = new ConcurrentHashMap<>();

	@Override // 오버라이드를 붙이면 세이브의 매개변수와 반환타입이 인터페이스와 같아야함. 다르면 에러남
	public void save(Account account) {
		accounts.put(account.getId(), account); // id를 키로, account를 값으로 넣음 -> id로 조회하면 계좌 객체를 반환할수있음

	}

	@Override
	public Optional<Account> findById(Long id) {
		return Optional.ofNullable(accounts.get(id)); // 계좌가 있으면 Account를 담는다 없으면 비어있는 Optional을 반환함 ofnullable로 구현한다
	}
}
// 디비를 붙이기전에 해쉬맵을 인메모리로 옮김 서비스가 해쉬맵에 의존하지 않게만듦
