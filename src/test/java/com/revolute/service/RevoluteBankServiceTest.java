package com.revolute.service;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.revolute.model.Account;
import com.revolute.model.OperationResult;
import com.revolute.service.RevolutBankService;
import com.revolute.service.RevolutBankServiceImpl;

public class RevoluteBankServiceTest {
	private RevolutBankService service = new RevolutBankServiceImpl();

	private void createAccountTest(String accountId) {
		OperationResult operationResult = service.createAccount(accountId);
		Assert.assertTrue(operationResult.isSuccess());
		operationResult = service.createAccount(accountId);
		Assert.assertFalse(operationResult.isSuccess());
	}

	@Test
	public void testAll() throws InterruptedException {
		String accountId1 = "shailendra";
		String accountId2 = "amit";
		OperationResult operationResult = service.addMoney(accountId1, 20000);
		Assert.assertFalse(operationResult.isSuccess());
		createAccountTest(accountId1);
		operationResult = service.addMoney(accountId1, 20000);
		Assert.assertTrue(operationResult.isSuccess());
		Account account = service.getAccountDetails(accountId1);
		Assert.assertEquals(account.getBalance(), 20000, 0);

		operationResult = service.withdrawMoney(accountId2, 1000);
		Assert.assertFalse(operationResult.isSuccess());
		createAccountTest(accountId2);

		operationResult = service.withdrawMoney(accountId2, 1000);
		Assert.assertFalse(operationResult.isSuccess());

		operationResult = service.addMoney(accountId2, 2000);

		Assert.assertTrue(operationResult.isSuccess());
		operationResult = service.withdrawMoney(accountId2, 1000);
		Assert.assertTrue(operationResult.isSuccess());

		Account account1 = service.getAccountDetails(accountId1);
		Account account2 = service.getAccountDetails(accountId2);

		double totalBalance = account1.getBalance() + account2.getBalance();

		
		ExecutorService service1 = Executors.newFixedThreadPool(5);
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			service1.submit(new Runnable() {

				@Override
				public void run() {
					service.transferMoney(accountId1, accountId2, r.nextInt(50));
					Account acc1 = service.getAccountDetails(accountId1);
					Account acc2 = service.getAccountDetails(accountId2);
					Assert.assertEquals(totalBalance, acc1.getBalance()+acc2.getBalance(),0);
				}
			});
			
		}

		ExecutorService service2 = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 1000; i++) {
			service2.submit(new Runnable() {

				@Override
				public void run() {
					service.transferMoney(accountId2, accountId1, r.nextInt(50));
					Account acc1 = service.getAccountDetails(accountId1);
					Account acc2 = service.getAccountDetails(accountId2);
					Assert.assertEquals(totalBalance, acc1.getBalance()+acc2.getBalance(),0);
				}
			});
			
		}
		
		
		service1.shutdown();
		service2.shutdown();
	
		
		try {
			service1.awaitTermination(60, TimeUnit.SECONDS);
			service2.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
	}
}
