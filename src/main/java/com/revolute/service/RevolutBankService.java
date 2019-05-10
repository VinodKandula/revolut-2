package com.revolute.service;

import com.revolute.model.Account;
import com.revolute.model.OperationResult;

public interface RevolutBankService {

	OperationResult createAccount(String accountId);

	Account getAccountDetails(String accountId);

	OperationResult addMoney(String accountId, double money);

	OperationResult withdrawMoney(String accountId, double money);

	OperationResult transferMoney(String fromAccountId, String toAccountId, double money);
}
