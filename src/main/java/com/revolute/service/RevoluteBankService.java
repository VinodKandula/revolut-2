package com.revolute.service;

import com.revolute.model.Account;
import com.revolute.model.OperationResult;

public interface RevoluteBankService {

	OperationResult createAccount(String accountId);

	Account getAccountDetails(String accountId);

	OperationResult deposite(String accountId, double money);

	OperationResult withdraw(String accountId, double money);

	OperationResult transfer(String fromAccountId, String toAccountId, double money);
}
