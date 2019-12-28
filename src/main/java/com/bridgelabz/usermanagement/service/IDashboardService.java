package com.bridgelabz.usermanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bridgelabz.usermanagement.model.User;
import com.bridgelabz.usermanagement.response.Response;

public interface IDashboardService {

	HashMap<String, Long> getUserStatistics();

	List<User> getLatestRegisteredUsers();

	HashMap<String, Double> getGenderPercentage(List<User> userList);

	Map<String, Integer> getTopCountries(List<User> userList);

	HashMap<String, Long> getAgeGroup(List<User> userList);

	Response getUserStat(int year, int month);

	Map<Integer, Integer> getAlltime();

	Map<Object, Integer> getAllByYear(Integer year);

	HashMap<Object, Long> getByMonthAndYear(int month, int year);
}
