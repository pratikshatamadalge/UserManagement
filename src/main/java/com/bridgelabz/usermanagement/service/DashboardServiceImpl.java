package com.bridgelabz.usermanagement.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.usermanagement.dto.DashDTO;
import com.bridgelabz.usermanagement.model.User;
import com.bridgelabz.usermanagement.repository.IRegisterRepository;
import com.bridgelabz.usermanagement.response.Response;

/**
 * purpose:Service class for dashboard
 * 
 * @author pratiksha
 *
 */
@Service
public class DashboardServiceImpl implements IDashboardService {

	@Autowired(required = true)
	IRegisterRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * purpose: method for statistics of registered users
	 */
	@Override
	public HashMap<String, Long> getUserStatistics() {
		List<User> userList = repository.findAll();
		HashMap<String, Long> map = new HashMap<String, Long>();
		long total = userList.size();
		long active = userList.stream().filter(c -> c.isActive()).count();
		long inactive = total - active;
		long online = userList.stream().filter(c -> c.isOnline()).count();
		map.put("total ", total);
		map.put("active ", active);
		map.put("inactive ", inactive);
		map.put("online ", online);
		return map;
	}

	/**
	 * purpose: method for getting details of latest registered users
	 */
	@Override
	public List<User> getLatestRegisteredUsers() {

		List<User> userList = repository.findAll();
		userList.sort((User user1, User user2) -> user1.getRegisteredDate().compareTo(user1.getRegisteredDate()));

		return userList;
	}

	/**
	 * 
	 * @param userList
	 * @return hashmap with percentage of male and female registered users
	 */
	@Override
	public HashMap<String, Double> getGenderPercentage(List<User> userList) {

		Double maleCount = (double) userList.stream().filter(x -> "Male".equals(x.getGender())).count();
		Double femaleCount = (double) userList.stream().filter(x -> "Female".equals(x.getGender())).count();

		HashMap<String, Double> gendercount = new HashMap<>();
		gendercount.put("Male", (maleCount / (maleCount + femaleCount) * 100));
		gendercount.put("Female", (femaleCount / (maleCount + femaleCount) * 100));
		return gendercount;
	}

	/**
	 * 
	 * @param userList
	 * @return map with highest registered users
	 */
	@Override
	public Map<String, Integer> getTopCountries(List<User> userList) {
		Map<String, List<User>> studlistGrouped = userList.stream().collect(Collectors.groupingBy(w -> w.getCountry()));
		Set<String> key = studlistGrouped.keySet();

		Map<String, Integer> topList = new LinkedHashMap<>();
		for (String string : key) {
			topList.put(string, Integer.valueOf(studlistGrouped.get(string).size()));
		}

		return topList.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue,
						LinkedHashMap::new));
	}

	/**
	 * 
	 * @param userList
	 * @return map containing number of registered users based on age group
	 */
	@Override
	public HashMap<String, Long> getAgeGroup(List<User> userList) {
		long ageLessThan18 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() < 18)
				.count();
		long ageGreaterThan42 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() > 42)
				.count();
		long ageBetween18and22 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() >= 18
				&& LocalDate.now().getYear() - x.getDob().getYear() <= 22).count();
		long ageBetween23and27 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() >= 23
				&& LocalDate.now().getYear() - x.getDob().getYear() <= 27).count();
		long ageBetween28and32 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() >= 28
				&& LocalDate.now().getYear() - x.getDob().getYear() <= 32).count();
		long ageBetween33and37 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() >= 33
				&& LocalDate.now().getYear() - x.getDob().getYear() <= 37).count();
		long ageBetween38and42 = userList.stream().filter(x -> LocalDate.now().getYear() - x.getDob().getYear() >= 38
				&& LocalDate.now().getYear() - x.getDob().getYear() <= 42).count();

		HashMap<String, Long> ageGroupCount = new LinkedHashMap<>();
		ageGroupCount.put("18-22", ageBetween18and22);
		ageGroupCount.put("23-27", ageBetween23and27);
		ageGroupCount.put("28-32", ageBetween28and32);
		ageGroupCount.put("33-37", ageBetween33and37);
		ageGroupCount.put("38-42", ageBetween38and42);
		ageGroupCount.put("Over42", ageGreaterThan42);
		ageGroupCount.put("Under18", ageLessThan18);

		return ageGroupCount;

	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return response with user registration history
	 */
	@Override
	public Response getUserStat(int year, int month) {

		DashDTO dashboard = new DashDTO();
		List<User> userList = null;
		if (year == 0 && month == 0) {
			userList = repository.findAll();
			dashboard.setUserRegistrationCount(getAlltime());
		} else if (month == 0) {
			userList = repository.findAll().stream().filter(x -> x.getRegisteredDate().getYear() == year)
					.collect(Collectors.toList());
			dashboard.setUserRegistrationCount(getAllByYear(year));
		} else if (year == 0) {
			userList = repository.findAll().stream()
					.filter(x -> x.getRegisteredDate().getYear() == LocalDate.now().getYear()
							&& x.getRegisteredDate().getMonthValue() == month)
					.collect(Collectors.toList());
			dashboard.setUserRegistrationCount(getByMonthAndYear(month, LocalDate.now().getYear()));
		} else {
			userList = repository.findAll().stream().filter(
					x -> x.getRegisteredDate().getYear() == year && x.getRegisteredDate().getMonthValue() == month)
					.collect(Collectors.toList());
			dashboard.setUserRegistrationCount(getByMonthAndYear(month, year));
		}

		dashboard.setTopCountries(getTopCountries(userList));
		dashboard.setGenderPercentage(getGenderPercentage(userList));
		dashboard.setAgeGroup(getAgeGroup(userList));
		return new Response(HttpStatus.OK, dashboard, "All time data");
	}

	/**
	 * 
	 * @return map containing number of registered users grouped by year
	 */
	@Override
	public Map<Integer, Integer> getAlltime() {
		List<User> userList = repository.findAll();
		Map<Integer, List<User>> yearGrouped = userList.stream()
				.collect(Collectors.groupingBy(w -> w.getRegisteredDate().getYear()));
		Set<Integer> key = yearGrouped.keySet();

		Map<Integer, Integer> topList = new LinkedHashMap<>();
		for (Integer string : key) {

			topList.put(string, Integer.valueOf(yearGrouped.get(string).size()));
		}

		topList = topList.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors
				.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, LinkedHashMap::new));
		return topList;
	}

	/**
	 * 
	 * @param year
	 * @return map containing number of registered users in particular year
	 */
	@Override
	public Map<Object, Integer> getAllByYear(Integer year) {
		List<User> userList = repository.findAll();

		Map<Object, List<User>> yearGrouped = userList.stream()
				.filter(x -> year.equals(x.getRegisteredDate().getYear()))
				.collect(Collectors.groupingBy(w -> w.getRegisteredDate().getMonth()));
		Set<Object> key = yearGrouped.keySet();

		Map<Object, Integer> topList = new LinkedHashMap<>();
		for (Object string : key) {

			topList.put(string, Integer.valueOf(yearGrouped.get(string).size()));
		}

		return topList;
	}

	/**
	 * 
	 * @param month
	 * @param year
	 * @return map containing number of registered users in particular month of
	 *         given year
	 */
	@Override
	public HashMap<Object, Long> getByMonthAndYear(int month, int year) {
		long count = repository.findAll().stream()
				.filter(i -> i.getRegisteredDate().getYear() == year && i.getRegisteredDate().getMonthValue() == month)
				.count();

		String[] monthName = { "January", "Febraury", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		HashMap<Object, Long> byMonthYear = new HashMap<>();
		byMonthYear.put(monthName[month - 1], count);
		return byMonthYear;
	}

}