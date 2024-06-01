package com.photogallery.photogallery.Request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	@NotBlank(message = "username must not be empty")
	private String username;

	@NotBlank(message = "password must not be empty")
	private String password;
}
