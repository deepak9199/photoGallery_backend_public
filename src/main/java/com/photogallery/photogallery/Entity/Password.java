package com.photogallery.photogallery.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Password {

	@NotBlank(message = "newpassword must not be empty")
	@Size(min = 6, max = 40)
	private String newpassword;

	@NotBlank(message = "conformpassword  must not be empty")
	@Size(min = 6, max = 40)
	private String conformpassword;
}
