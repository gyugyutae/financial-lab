package com.gyutae.financiallab.account.application;

import java.util.Optional;

import com.gyutae.financiallab.account.domain.Account;

public interface AccountRepository { // 인터페이스로 두는 이유는 저장소가 제공할 기능의 약속.
	void save(Account account);

	Optional<Account> findById(Long id); // Account로 반환해서 ID와 잔액을 모두알수있음
	// 맵에 계죄가 있으면 Account를 담는다 없으면 비어있는 Optional을 반환함 ofnullable로 구현한다

}
// repo는 저장방식만 담당한다 그래서 optional로 있음 없음만 반한다 서비스는 없으면 notFOundException을 던짐
// 서비스가 레포에 요청만 보내면되니까 저장방식을 나중에 바꿀때 서비스를 손안보고 레포만 손보면된다
