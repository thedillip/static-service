package com.dillip.api.response;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class ApiResponseObject implements Serializable {
	private LocalDateTime timestamp;
	private String message;

	public ApiResponseObject() {
		this.timestamp = LocalDateTime.now();
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiResponseObject [timestamp=" + timestamp + ", message=" + message + "]";
	}
}
