package com.gyutae.ledgerlab.account.api;

/**
 * 에러 응답을 나타내는 레코드
 */
public record ErrorResponse(
		String code, // account not found
		String message // 계좌를 찾을수없습니다. id=
) {

}
