package com.photogallery.photogallery.Exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.photogallery.photogallery.Response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// handling global exception
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ApiResponse<?> handleGlobalException(Exception exception) {
		log.error("Exception : {}", exception.getMessage());
		return ApiResponse.failure(exception.getMessage());
	}

	// handling specific exception
	@ExceptionHandler(ApiException.class)
	@ResponseBody
	public ApiResponse<?> handleApiException(ApiException apiException) {
		log.error("API Exception : {}", apiException.getMessage());
		return ApiResponse.failure(apiException.getCode(), apiException.getMessage());
	}

	// handling specific exception
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException exception) {
		log.error("ResourceNotFoundException Exception : {}", exception.getMessage());
		return ApiResponse.failure(exception.getMessage());
	}

}
