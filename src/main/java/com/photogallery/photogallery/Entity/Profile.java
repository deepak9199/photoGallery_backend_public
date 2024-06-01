package com.photogallery.photogallery.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "userid must not be Null")
	@Min(value = 1, message = "userid must not be 0")
	private Long userid;

	@NotBlank(message = "companyname must not be empty")
	@NotBlank
	@Size(max = 500)
	private String companyname;

	@NotBlank(message = "gstno must not be empty")
	@NotBlank
	@Size(max = 500)
	private String gstno;

	@NotBlank(message = "statecode must not be empty")
	@NotBlank
	@Size(max = 500)
	private String statecode;

	@Size(max = 500)
	private String mobileno;

	@Size(max = 500)
	private String addressheading;

	@Size(max = 500)
	private String addresstitle;

	@Size(max = 500)
	private String email;

	@Size(max = 500)
	private String gmailsecoundrypassword;

	@Size(max = 500)
	private String downloadfilepath;

}
