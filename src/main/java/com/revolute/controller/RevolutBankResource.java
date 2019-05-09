package com.revolute.controller;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.revolute.model.OperationResult;
import com.revolute.service.RevolutBankService;

@Path("/revolut")
@Singleton
public class RevolutBankResource {

	@Inject
	RevolutBankService revolutBankService;

	@POST
	@Path("/account/{accountId}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult createAccount(@PathParam("accountId") String accountId) {
		return revolutBankService.createAccount(accountId);

	}

	@GET
	@Path("/account/{accountId}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult getAccountDetails(@PathParam("accountId") String accountId) {
		return revolutBankService.getAccountDetails(accountId);

	}

	@POST
	@Path("/account/{accountId}/deposite/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult deposite(@PathParam("accountId") String accountId, @PathParam("amount") double amount) {
		return revolutBankService.addMoney(accountId, amount);

	}

	@POST
	@Path("/account/{accountId}/withdraw/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult withdraw(@PathParam("accountId") String accountId, @PathParam("amount") double amount) {
		return revolutBankService.withdrawMoney(accountId, amount);

	}

	@POST
	@Path("/transfer/{fromAccountId}/{toAccountId}/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult tranfer(@PathParam("fromAccountId") String fromAccountId,
			@PathParam("toAccountId") String toAccountId, @PathParam("amount") double amount) {
		return revolutBankService.transferMoney(fromAccountId, toAccountId, amount);

	}

}
