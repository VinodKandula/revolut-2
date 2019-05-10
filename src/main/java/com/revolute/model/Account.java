package com.revolute.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Account {

	private String accountId;

	private double balance;

	public Account() {

	}

	public Account(String accountId) {
		this.accountId = accountId;
		this.balance = 0.0;
	}

	public String getAccountId() {
		return accountId;
	}

	@XmlElement
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@XmlElement
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void addMoney(double money) {
		this.balance = balance + money;
	}

	public void withdrawMoney(double money) {
		if (money > balance) {
			throw new RuntimeException("Insufficient Fund to withdraw!!");
		}
		balance = balance - money;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", balance=" + balance + "]";
	}

}
