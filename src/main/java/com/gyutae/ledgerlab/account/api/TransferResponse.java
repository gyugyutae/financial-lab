package com.gyutae.ledgerlab.account.api;

import java.math.BigDecimal;

public record TransferResponse(
		Long fromAccountId,
		Long toAccountId,
		BigDecimal fromBalance,
		BigDecimal toBalance) {
}
// 컨트롤러가 클라이언트에게 보내는거임 외부에 공개할 결과를 담음
// 레코드는 클래스의 종류로 단순히 값을 담고 전달하는데 특화됨 리퀘스트 리스판스 리절트처럼 데이터를 전달하는객체에 잘어울림
