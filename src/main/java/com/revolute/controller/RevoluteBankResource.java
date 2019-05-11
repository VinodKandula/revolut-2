package com.revolute.controller;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.revolute.model.Account;
import com.revolute.model.OperationResult;
import com.revolute.service.RevoluteBankService;

@Path("/revolute")
@Singleton
public class RevoluteBankResource {

	@Inject
	RevoluteBankService revoluteBankService;

	@POST
	@Path("/account/{accountId}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult createAccount(@PathParam("accountId") String accountId) {
		return revoluteBankService.createAccount(accountId);

	}

	@GET
	@Path("/account/{accountId}")
	@Produces(MediaType.APPLICATION_XML)
	public Account getAccountDetails(@PathParam("accountId") String accountId) {
		return revoluteBankService.getAccountDetails(accountId);

	}

	@POST
	@Path("/account/{accountId}/deposite/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult deposite(@PathParam("accountId") String accountId, @PathParam("amount") double amount) {
		return revoluteBankService.deposite(accountId, amount);

	}

	@POST
	@Path("/account/{accountId}/withdraw/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult withdraw(@PathParam("accountId") String accountId, @PathParam("amount") double amount) {
		return revoluteBankService.withdraw(accountId, amount);

	}

	@POST
	@Path("/transfer/{fromAccountId}/{toAccountId}/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public OperationResult tranfer(@PathParam("fromAccountId") String fromAccountId,
			@PathParam("toAccountId") String toAccountId, @PathParam("amount") double amount) {
		return revoluteBankService.transfer(fromAccountId, toAccountId, amount);

	}

}
