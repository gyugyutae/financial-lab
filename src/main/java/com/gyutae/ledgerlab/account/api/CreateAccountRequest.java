package com.gyutae.ledgerlab.account.api;

import java.math.BigDecimal;

/**
 * 계좌를 생성하기 위한 요청 데이터
 */
public record CreateAccountRequest(
		BigDecimal initialBalance) {
}
