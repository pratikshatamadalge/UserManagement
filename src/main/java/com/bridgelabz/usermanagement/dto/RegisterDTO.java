package com.bridgelabz.usermanagement.dto;

import java.util.Date;

import org.springframework.lang.NonNull;

import lombok.Data;

/**
 * Purpose: DTO class for Register
 * 
 * @author pratiksha
 */
@Data
public class RegisterDTO {
	@NonNull
	private String firstName;
	@NonNull
	private String middleName;
	@NonNull
	private String lastName;
	@NonNull
	private Date dob;
	@NonNull
	private String gender;
	@NonNull
	private String country;
	@NonNull
	private String mobile;
	@NonNull
	private String phoneExt;
	@NonNull
	private String emailId;
	@NonNull
	private String address;
	@NonNull
	private String userName;
	@NonNull
	private String password;
	@NonNull
	private String confirmPassword;
	@NonNull
	private String userRole;
	private String profilePic;
}
