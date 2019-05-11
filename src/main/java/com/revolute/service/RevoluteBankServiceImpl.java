package com.revolute.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.revolute.model.Account;
import com.revolute.model.OperationResult;

public class RevoluteBankServiceImpl implements RevoluteBankService {

	private Map<String, Account> accounts = new HashMap<>();
	private Map<String, Lock> accountsLock = new HashMap<>();

	public RevoluteBankServiceImpl() {

	}

	public OperationResult createAccount(String accountId) {

		try {
			checkNull(accountId);
			if (!accounts.containsKey(accountId)) {
				synchronized (RevoluteBankServiceImpl.class) {
					if (accounts.containsKey(accountId)) {
						throw new IllegalArgumentException("Account Already Exists");
					}
					accounts.put(accountId, new Account(accountId));
					accountsLock.put(accountId, new ReentrantLock(true));
					return new OperationResult(true, "Account Created Successfully!!");
				}
			} else {
				throw new IllegalArgumentException("Account Already Exists");
			}
		} catch (Exception e) {
			return new OperationResult(false, e.getMessage());
		}
	}

	public OperationResult deposite(String accountId, double money) {
		try {
			checkNull(accountId);
			Account account = accounts.get(accountId);
			if (account != null) {
				try {
					acquireLock(accountsLock.get(accountId));
					account.addMoney(money);
					return new OperationResult(true, "Added Money Successfully!!");
				} finally {
					accountsLock.get(accountId).unlock();
				}
			} else {
				throw new IllegalArgumentException("Invaild AccountId..");
			}
		} catch (Exception e) {
			return new OperationResult(false, e.getMessage());
		}

	}

	public OperationResult withdraw(String accountId, double money) {
		try {
			checkNull(accountId);
			Account account = accounts.get(accountId);
			if (account != null) {
				try {
					acquireLock(accountsLock.get(accountId));
					account.withdrawMoney(money);
					return new OperationResult(true, "Withdraw Money Successfully!!");
				} finally {
					accountsLock.get(accountId).unlock();
				}
			} else {
				throw new IllegalArgumentException("Invaild AccountId..");
			}
		} catch (Exception e) {
			return new OperationResult(false, e.getMessage());
		}
	}

	public OperationResult transfer(String fromAccountId, String toAccountId, double money) {
		try {
			checkNull(fromAccountId);
			checkNull(toAccountId);
			if (fromAccountId.equals(toAccountId)) {
				throw new IllegalArgumentException("From and To Account Ids can not be same");
			}
			Account fromAccount = accounts.get(fromAccountId);
			if (fromAccount == null) {
				throw new IllegalArgumentException("Account Id:" + fromAccountId + " does not exists.");
			}
			Account toAccount = accounts.get(toAccountId);
			if (toAccount == null) {
				throw new IllegalArgumentException("Account Id:" + toAccountId + " does not exists.");
			}

			try {
				Lock fromAccountLock = accountsLock.get(fromAccountId);
				Lock toAccountLock = accountsLock.get(toAccountId);
				acquireLocks(fromAccountLock, toAccountLock);

				fromAccount.withdrawMoney(money);
				toAccount.addMoney(money);
				return new OperationResult(true, "Transfer Money Successfully!!");
			} finally {
				accountsLock.get(toAccountId).unlock();
				accountsLock.get(fromAccountId).unlock();
			}
		} catch (Exception e) {
			return new OperationResult(false, e.getMessage());
		}
	}

	private void acquireLock(Lock accountLock) {
		while (true) {
			boolean isLocked = accountLock.tryLock();
			if (isLocked) {
				return;
			}
		}
	}

	private void acquireLocks(Lock fromAccountLock, Lock toAccountLock) {
		while (true) {
			boolean gotFromLock = false;
			boolean gotToLock = false;

			try {
				gotFromLock = fromAccountLock.tryLock();
				gotToLock = toAccountLock.tryLock();
			} finally {
				if (gotFromLock && gotToLock) {
					return;
				}
				if (gotFromLock) {
					fromAccountLock.unlock();
				}
				if (gotToLock) {
					toAccountLock.unlock();
				}
			}
		}

	}

	private void checkNull(String accountId) {
		if (accountId == null) {
			throw new NullPointerException("AccountId can't be null");
		}
	}

	@Override
	public Account getAccountDetails(String accountId) {
		try {
			checkNull(accountId);
			Account account = accounts.get(accountId);
			if (account == null) {
				throw new IllegalArgumentException("Acoount does not exists.");
			}

			return account;
		} catch (Exception e) {
			return null;
		}

	}

}
