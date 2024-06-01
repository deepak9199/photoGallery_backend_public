package com.photogallery.photogallery.Request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PasswordRequest {

	@NotBlank(message = "username must not be empty")
	@Size(max = 120)
	private String username;

	private String oldpassword;
	
	@NotBlank(message = "newpassword must not be empty")
	@NotBlank
	@Size(min = 6, max = 40)
	private String newpassword;

	@NotBlank(message = "conformpassword  must not be empty")
	@NotBlank
	@Size(min = 6, max = 40)
	private String conformpassword;

	@NotBlank(message = "passcode must not be empty")
	@Size(max = 120)
	private String passcode;
}
