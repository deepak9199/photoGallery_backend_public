package com.photogallery.photogallery.Response;

import java.io.Serializable;
import java.util.List;

import com.photogallery.photogallery.Constant.ReturnCode;
import com.photogallery.photogallery.Exception.ApiStatus;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class ApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private ApiStatus apiStatus;
	private T data;

	public static <T> ApiResponse<Object> success(T data) {
		ApiStatus apiStatus = new ApiStatus(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage());
		ApiResponse<Object> apiResponse = ApiResponse.builder().apiStatus(apiStatus).data(data).build();
		log.info("Success API Response: {}", apiResponse.toString());
		return apiResponse;
	}

	public static ApiResponse<Object> success() {
		return success(null);
	}

	public static ApiResponse<Object> failure(int code, String message, List<String> errors) {
		ApiStatus apiStatus = new ApiStatus(code, message, errors);
		ApiResponse<Object> apiResponse = ApiResponse.builder().apiStatus(apiStatus).build();
		return apiResponse;
	}

	public static ApiResponse<Object> failure(int code, String message) {
		ApiStatus apiStatus = new ApiStatus(code, message);
		ApiResponse<Object> apiResponse = ApiResponse.builder().apiStatus(apiStatus).build();
		return apiResponse;
	}

	public static <T> ApiResponse<Object> failure(ReturnCode returnCode) {
		return failure(returnCode.getCode(), returnCode.getMessage());
	}

	public static <T> ApiResponse<Object> failure(String message) {
		return failure(ReturnCode.FAILED.getCode(), message);
	}

	public static <T> ApiResponse<Object> failure() {
		return failure(ReturnCode.FAILED);
	}

	public static <T> ApiResponse<Object> failure(ReturnCode returnCode, List<String> errors) {
		return failure(returnCode.getCode(), returnCode.getMessage(), errors);
	}

	public static <T> ApiResponse<Object> failure(List<String> errors) {
		return failure(ReturnCode.FAILED, errors);
	}

}
