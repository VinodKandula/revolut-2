package com.revolute.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.revolute.model.Account;
import com.revolute.model.OperationResult;

public class RevolutBankServiceImpl implements RevolutBankService {

	private Map<String, Account> accounts = new ConcurrentHashMap<>();
	private Map<String, Lock> accountsLock = new ConcurrentHashMap<>();

	public RevolutBankServiceImpl() {

	}

	public OperationResult createAccount(String accountId) {

		try {
			checkNull(accountId);
			if (!accounts.containsKey(accountId)) {
				synchronized (RevolutBankServiceImpl.class) {
					if (accounts.containsKey(accountId)) {
						throw new IllegalArgumentException("Account Already Exists");
					}
					accounts.put(accountId, new Account(accountId));
					return new OperationResult(true, "Account Created Successfully!!");
				}
			} else {
				throw new IllegalArgumentException("Account Already Exists");
			}
		} catch (Exception e) {
			return new OperationResult(false, e.getMessage());
		}
	}

	public OperationResult addMoney(String accountId, double money) {
		try {
			checkNull(accountId);
			Account account = accounts.get(accountId);
			if (account != null) {
				checkForLocks(accountId);

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

	public OperationResult withdrawMoney(String accountId, double money) {
		try {
			checkNull(accountId);
			Account account = accounts.get(accountId);
			if (account != null) {
				checkForLocks(accountId);
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

	private void checkForLocks(String accountId) {
		if (accountsLock.get(accountId) == null) {
			synchronized (RevolutBankServiceImpl.class) {
				if (accountsLock.get(accountId) == null)
					accountsLock.put(accountId, new ReentrantLock());
			}
		}
	}

	public OperationResult transferMoney(String fromAccountId, String toAccountId, double money) {
		try {
			checkNull(fromAccountId);
			checkNull(toAccountId);
			if (fromAccountId.equals(toAccountId)) {
				throw new IllegalArgumentException("From and To Account Ids can not be same");
			}
			Account fromAccount = accounts.get(fromAccountId);
			if (fromAccount == null) {
				throw new IllegalArgumentException("Account Id:" + fromAccount + " does not exists.");
			}
			Account toAccount = accounts.get(toAccountId);
			if (toAccount == null) {
				throw new IllegalArgumentException("Account Id:" + toAccount + " does not exists.");
			}
			checkForLocks(fromAccountId);
			checkForLocks(toAccountId);

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
	public OperationResult getAccountDetails(String accountId) {
		try {
			checkNull(accountId);
			Account account = accounts.get(accountId);
			if (account == null) {
				throw new IllegalArgumentException("Acoount does not exists.");
			}
			return new OperationResult(true, "Balance :" + account.getBalance());
		} catch (Exception e) {
			return new OperationResult(false, e.getMessage());
		}

	}

}
