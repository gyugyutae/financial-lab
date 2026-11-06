package com.gyutae.ledgerlab.account.application;

import com.gyutae.ledgerlab.account.domain.Account;

public record TransferResult(
		Account fromAccount,
		Account toAccount) {

}
// 서비스가 컨트롤러에게 보내는거임 내부에서 처리한 이체 결과를 담음
