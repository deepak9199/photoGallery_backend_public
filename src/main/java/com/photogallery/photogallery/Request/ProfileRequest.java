package com.photogallery.photogallery.Request;

import lombok.Data;

@Data
public class ProfileRequest {

	private Long userid;

	private String companyname;

	private String gstno;

	private String statecode;

	private String mobileno;

	private String addressheading;

	private String addresstitle;

	private String email;

	private String passcode;

	private String gmailsecoundrypassword;

	private String downloadfilepath;
}
