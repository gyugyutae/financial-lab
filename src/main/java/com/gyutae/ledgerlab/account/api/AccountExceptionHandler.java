package com.gyutae.ledgerlab.account.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gyutae.ledgerlab.account.application.AccountNotFoundException;
import com.gyutae.ledgerlab.account.domain.InsufficientBalanceException;

/**
 * 계좌 관련 예외를 처리하는 클래스
 */
@RestControllerAdvice // 예외를 이 클래스에서 처리
public class AccountExceptionHandler {
	@ExceptionHandler(AccountNotFoundException.class) // AccountNotFoundException이 발생하면 이 메서드가 실행됨
	@ResponseStatus(HttpStatus.NOT_FOUND) // 404에러를 반환하도록 설정
	public ErrorResponse handleAccountNotFound( // 메서드이름은 자유롭게가능
			AccountNotFoundException exception) { // accountNotFoundException 타입의 예외객체를 exception이라는 변수로 받음
		return new ErrorResponse( // 오류 응답 객체 생성후 반환
				"ACCOUNT_NOT_FOUND",
				exception.getMessage() // super에 저장했던 메시지를 꺼냄
		);
	}

	@ExceptionHandler(InsufficientBalanceException.class) // InsufficientBalanceException이 발생하면 이 메서드가 실행됨
	@ResponseStatus(HttpStatus.CONFLICT) // CONFLICT 409에러를 반환하도록 설정
	public ErrorResponse handleInsufficientBalance(
			InsufficientBalanceException exception) {
		return new ErrorResponse(
				"INSUFFICIENT_BALANCE",
				exception.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class) // 스프링은 변수를 보고 실행하는게 아니라 이 에너테이션을 보고실행함
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleIllegalArgument( // 이메서드의 반환타입은 errorResponse, 매개변수는 IllegalArgumentException
			IllegalArgumentException exception) { // 매개변수임
		return new ErrorResponse(
				"ILLEGAL_ARGUMENT",
				exception.getMessage());
	}
}
/*
 * /
 * 꼭알아야할 http 상태 코드
 * 1xx 처리중
 * 2xx 성공
 * 4xx클라이언트문제
 * 5xx서버문제
 *
 * 201 Created: 요청이 성공적으로 수행되어 새로운 리소스가 생성됨
 * 204 No Content: 요청이 성공적으로 수행되었지만 반환할 콘텐츠가 없음
 * 400 Bad Request: 잘못된 요청, 서버가 요청을 이해할 수 없음
 * 401 Unauthorized: 인증이 필요함, 인증되지 않은 사용자
 * 403 Forbidden: 서버가 요청을 이해했지만 권한이 없음
 * 404 Not Found: 요청한 리소스를 찾을 수 없음
 * 409 Conflict: 요청이 현재 서버 상태와 충돌함, 예를 들어 중복된
 * 429 Too Many Requests: 클라이언트가 너무 많은 요청을 보냄, 서버가 요청을 처리할 수 없음
 * 500 Internal Server Error: 클라이언트가아니라 서버가 예상치못한 문제
 * 502 Bad Gateway: 우리서버가 다른서버에서 잘못된 응답
 * 503 Service Unavailable: 서버가 일시적으로 과부하 또는 유지보수 중
 * /
 */
