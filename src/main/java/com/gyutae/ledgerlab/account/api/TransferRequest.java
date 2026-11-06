package com.gyutae.ledgerlab.account.api;

import java.math.BigDecimal;

public record TransferRequest(
		Long toAccountId,
		BigDecimal amount) {
}
// 클라이언트는 누구에게 얼마를 보낼지만은 보낸다 fromaccounid는 url로 받는다
// 클라이언트가 컨트롤러에보내는거임 무엇을 이체할지 담음
// DTO는 요청이랑 응답을 할때 따로 두면 클라이언트가 보낼수있는 값과 서버가 공개할 값을 구분하기쉬움
// 레코드는 이런 값 전달용 객체를 간결하게 작성할때 편리함
