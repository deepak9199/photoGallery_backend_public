package com.photogallery.photogallery.Exception;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class ApiStatus {

	private int code;
    private String message;
    private List<String> errors;

    public ApiStatus(int code, String message, List<String> errors) {
        super();
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public ApiStatus(int code, String message, String error) {
        super();
        this.code = code;
        this.message = message;
        errors = Arrays.asList(error);
    }
    
    public ApiStatus(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
