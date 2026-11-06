package com.gyutae.ledgerlab.account.api;
// controller는 처음에 http로 요청을받으면 json으로 변환해서 request에게 보냄

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 컨트롤러는 서비스가 필요한데 직접만들지않고 에너테이션을 사용해서 스프링이 만들어서 생성자로 보냄
// 스프링아 클래스 컨트롤러 객체로만들어서 관리해줘
// 컨트롤러는 계좌를 새성하기위해 service가필요함
// 생성자 주입(DI)
// post/accounts 를 이 메서드가 처리, 계좌를 새로만들것이므로 post
// json request를 createaccountrequest를 전달받고 accountresponse를 보낸다
// acount에서 값을꺼내 response생성한다

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gyutae.ledgerlab.account.application.AccountService;
import com.gyutae.ledgerlab.account.application.TransferResult;
import com.gyutae.ledgerlab.account.domain.Account;

@RestController
@RequestMapping("/accounts") // base url을 지정, /accounts로 시작하는 요청은 이 컨트롤러가 처리
public class AccountController {
	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping // post 요청이들어오면 메서드를 실행한다
	public AccountResponse createAccount(
			@RequestBody CreateAccountRequest request // 클라이언트는 json 요청을 보낸다 java객체로 변경 new createaccountrequest를 만들어서
														// request에 넣어줌 요청이 하나의 값만 가지고 있어도 요청 객체를 별도로 만들어야함
	) {
		BigDecimal initialBalance = request.initialBalance();
		Account account = accountService.createAccount(initialBalance); // service호출

		return new AccountResponse(account.getId(), account.getBalance()); // "id":1, "balance":1000 이런식으로 json으로 요청한
																			// 클라이언트에게 반환됨
	}

	@GetMapping("/{id}") // get이랑 id가 들어오면 메서드를 실행한다
	public AccountResponse getAccount(
			@PathVariable Long id) {
		Account account = accountService.findAccount(id);

		return new AccountResponse(account.getId(), account.getBalance());
	}

	@PostMapping("/{id}/deposits")
	public AccountResponse deposit(
			@PathVariable Long id, // url에 있는 id를 가져와서 Long id에 넣어줌
			@RequestBody DepositRequest request) {
		BigDecimal amount = request.amount();
		Account account = accountService.deposit(id, amount);
		return new AccountResponse(account.getId(), account.getBalance());
	}

	@PostMapping("/{id}/withdrawals")
	public AccountResponse withdraw(
			@PathVariable Long id,
			@RequestBody WithdrawRequest request) {
		BigDecimal amount = request.amount();
		Account account = accountService.withdraw(id, amount);
		return new AccountResponse(account.getId(), account.getBalance());
	}

	@PostMapping("/{fromAccountId}/transfers") // restful 하게하려면 명사를 사용하고 복수형으로 통일함
	public TransferResponse transfer(
			@PathVariable Long fromAccountId, // from은 url로 받음
			@RequestBody TransferRequest request) { // to는 request로 받음
		BigDecimal amount = request.amount();
		Long toAccountId = request.toAccountId();
		TransferResult result = accountService.transfer(fromAccountId, toAccountId, amount);
		Account fromAccount = result.fromAccount();
		Account toAccount = result.toAccount();
		return new TransferResponse(
				fromAccount.getId(),
				toAccount.getId(),
				fromAccount.getBalance(),
				toAccount.getBalance());
	} // 리퀘스트 -> 서비스 -> 리절트 -> 리스판스
}
