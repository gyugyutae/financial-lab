package com.gyutae.ledgerlab.account.api;

import java.math.BigDecimal;

public record WithdrawRequest(
		BigDecimal amount) {

}
