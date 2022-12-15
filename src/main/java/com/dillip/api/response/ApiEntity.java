package com.dillip.api.response;

@SuppressWarnings("serial")
public class ApiEntity<T> extends ApiResponseObject{
	
	private T data;

	public ApiEntity() {
		super();
	}

	public ApiEntity(T data) 
	{
		super();
		this.data = data;
	}
	
	public ApiEntity(String message, T data)
	{
		super();
		setMessage(message);
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ApiEntity [data=" + data + "]";
	}
}
