package com.example.rqchallenge.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class RestResponse<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<T> data;
	private ApiError exception;

	public RestResponse() {}
	
	public RestResponse(List<T> data, ApiError exception) {
		super();
		this.data = data;
		this.exception = exception;
	}

	public RestResponse(T object) {
		data = Collections.singletonList(object);
	}

	public RestResponse(List<T> objects) {
		data = objects;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public ApiError getException() {
		return exception;
	}

	public void setException(ApiError exception) {
		this.exception = exception;
	}
	
	public static <T> RestResponse<T> build(T object) {
		return new RestResponse<T>(object);
	}
	
	public static <T> RestResponse<T> build(List<T> objects) {
		return new RestResponse<T>(objects);
	}

	@Override
	public String toString() {
		return "RestResponse [data=" + data + ", exception=" + exception + "]";
	}
	
}
