package com.bridgelabz.usermanagement.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DashDTO {
	private Map<String, Long> ageGroup;
	private HashMap<String, Double> genderPercentage;
	private Map<String, Integer> topCountries;
	
	private Map userRegistrationCount;
}
