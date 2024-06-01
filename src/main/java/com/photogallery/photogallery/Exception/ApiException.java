package com.photogallery.photogallery.Exception;



import com.photogallery.photogallery.Constant.ReturnCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	public ApiException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ApiException(ReturnCode returnCode) {
		super(returnCode.getMessage());
		this.code = returnCode.getCode();
	}
}
