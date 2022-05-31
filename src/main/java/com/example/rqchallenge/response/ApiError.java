package com.example.rqchallenge.response;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApiError implements Serializable {

	private String code;
	private String message;

	public ApiError() {}
	
	public ApiError(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiError [code=" + code + ", message=" + message + "]";
	}

}
