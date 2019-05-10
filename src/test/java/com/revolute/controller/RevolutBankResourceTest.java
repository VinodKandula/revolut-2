package com.revolute.controller;

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
			Assert.assertEquals(450,account.getBalance(),0);
		} finally {
			response.close();
		}
		
		target = client.target("http://localhost:8080/revolut/account/shailendra");
		response = target.request().get();
		try {
			Assert.assertEquals(200, response.getStatus());
			Account account = response.readEntity(Account.class);
			Assert.assertEquals(100,account.getBalance(),0);
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

	}

}
