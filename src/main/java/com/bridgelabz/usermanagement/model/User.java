package com.bridgelabz.usermanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Purpose:Model class for user information
 * 
 * @author pratiksha
 *
 */
@Document(collection = "User")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {
	@Id
	private String emailId;
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDate dob;
	private String gender;
	private String country;
	private String mobile;
	private String phoneExt;
	private String address;
	private String userName;
	private String password;
	private String confirmPassword;
	private String userRole;
	private String profilePic;
	private Boolean verified;
	private LocalDate registeredDate;
	private boolean isOnline;
	private boolean isActive;
	private ArrayList<Date> loginHistoty = new ArrayList<>();
	private HashMap<String, HashMap<String, Boolean>> permissions = new HashMap<String, HashMap<String, Boolean>>();
}
