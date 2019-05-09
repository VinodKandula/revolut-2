package com.revolute.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OperationResult {

	private boolean isSuccess;
	private String response;

	public OperationResult() {
	}

	public OperationResult(boolean isSuccess, String response) {
		this.isSuccess = isSuccess;
		this.response = response;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	@XmlElement
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getResponse() {
		return response;
	}

	@XmlElement
	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "OperationResult [isSuccess=" + isSuccess + ", response=" + response + "]";
	}

}
