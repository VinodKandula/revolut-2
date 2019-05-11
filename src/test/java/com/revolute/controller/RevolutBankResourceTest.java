package com.revolute.controller;

import static org.junit.Assert.assertEquals;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Binder;
import com.revolute.controller.util.JettyGuiceRestEasyTest;
import com.revolute.model.Account;
import com.revolute.model.OperationResult;
import com.revolute.service.RevolutBankService;
import com.revolute.service.RevolutBankServiceImpl;

public class RevolutBankResourceTest extends JettyGuiceRestEasyTest {
	@Override
	protected void configure(Binder b) {
		b.bind(RevolutBankResource.class);
		b.bind(RevolutBankService.class).toInstance(new RevolutBankServiceImpl());
	}

	@Test
	public void testAll() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/revolut/account/shailendra");
		Response response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			Account account = response.readEntity(Account.class);
			Assert.assertEquals("shailendra", account.getAccountId());
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/account/shailendra/deposite/100");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/account/shailendra/withdraw/50");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/account/shailendra");
		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			Account account = response.readEntity(Account.class);
			Assert.assertEquals("shailendra", account.getAccountId());
			Assert.assertEquals(50, account.getBalance(), 0);
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/account/amit");
		response = target.request().get();
		try {
			Assert.assertEquals(204, response.getStatus());
			Account account = response.readEntity(Account.class);
			Assert.assertNull(account);
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/account/amit");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/account/amit/deposite/500");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/transfer/amit/shailendra/50");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/account/amit");
		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			Account account = response.readEntity(Account.class);
			Assert.assertEquals(450, account.getBalance(), 0);
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/account/shailendra");
		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			Account account = response.readEntity(Account.class);
			Assert.assertEquals(100, account.getBalance(), 0);
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/transfer/shailendra/shailendra/100");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertFalse(operationResult.isSuccess());
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/transfer/shailendra/amit/500");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertFalse(operationResult.isSuccess());
		} finally {
			response.close();

		}

		client.close();

		testMultipleRequest();

	}

	private void testMultipleRequest() {

		CountDownLatch latch = new CountDownLatch(2000);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/revolut/account/xyz");
		Response response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/account/abc");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();
		}

		target = client.target("http://localhost:8080/revolut/account/xyz/deposite/50000");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		target = client.target("http://localhost:8080/revolut/account/abc/deposite/30000");
		response = target.request().post(null);
		try {
			Assert.assertEquals(200, response.getStatus());
			OperationResult operationResult = response.readEntity(OperationResult.class);
			Assert.assertTrue(operationResult.isSuccess());
		} finally {
			response.close();

		}

		double totalBalance = 80000;

		ExecutorService service1 = Executors.newFixedThreadPool(5);
		Random random1 = new Random();

		for (int i = 0; i < 1000; i++) {
			service1.submit(new Runnable() {

				@Override
				public void run() {
					Client client = ClientBuilder.newClient();
					int x = random1.nextInt(100) + 1;
					WebTarget target = client.target("http://localhost:8080/revolut/transfer/xyz/abc/" + x);
					Response response = target.request().post(null);
					try {
						Assert.assertEquals(200, response.getStatus());
						OperationResult operationResult = response.readEntity(OperationResult.class);
						Assert.assertTrue(operationResult.isSuccess());
					} finally {
						response.close();
						client.close();
					}
					latch.countDown();
				}
			});
		}

		ExecutorService service2 = Executors.newFixedThreadPool(5);
		Random random2 = new Random();

		for (int i = 0; i < 1000; i++) {
			service2.submit(new Runnable() {

				@Override
				public void run() {
					Client client = ClientBuilder.newClient();
					int x = random2.nextInt(100) + 1;
					WebTarget target = client.target("http://localhost:8080/revolut/transfer/abc/xyz/" + x);
					Response response = target.request().post(null);
					try {
						Assert.assertEquals(200, response.getStatus());
						OperationResult operationResult = response.readEntity(OperationResult.class);
						Assert.assertTrue(operationResult.isSuccess());
					} finally {
						response.close();
						client.close();
					}

					latch.countDown();
				}
			});
		}

		service1.shutdown();
		service2.shutdown();

		try {
			latch.await();
		} catch (InterruptedException e) {

		}

		Account accountAbc = null;
		target = client.target("http://localhost:8080/revolut/account/abc");
		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			accountAbc = response.readEntity(Account.class);

		} finally {
			response.close();
		}

		Account accountXyz = null;
		target = client.target("http://localhost:8080/revolut/account/xyz");
		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			accountXyz = response.readEntity(Account.class);

		} finally {
			response.close();
		}

		assertEquals(totalBalance, accountAbc.getBalance() + accountXyz.getBalance(), 0);

		client.close();

	}

}
