package com.gyutae.ledgerlab.account.api;

import java.math.BigDecimal;

public record AccountResponse( // record를 쓰면 단순한 객체를 짧게 만들수있음 DTO를 만들때 record를 쓰면 좋음
		Long id,
		BigDecimal balance) {

}

// 이렇게 하면 나중에 비밀번호, 고객 내부 ID, 계좌상태 같은 필드가 생겼을때 이렇게 별도로 record를 만들어놓으면 필요한 정보만
// 보낼수있다.
// 꺼내기를 할때는 response.id();
