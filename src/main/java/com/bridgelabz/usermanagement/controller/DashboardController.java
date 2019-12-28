package com.bridgelabz.usermanagement.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.usermanagement.model.User;
import com.bridgelabz.usermanagement.response.Response;
import com.bridgelabz.usermanagement.service.IDashboardService;

/**
 * Purpose: To implement dashboard controller
 * 
 * @author Pratiksha
 *
 */
@RestController
@RequestMapping("/statistics")
public class DashboardController {
	@Autowired(required = true)
	IDashboardService dashboardService;

	/**
	 * purpose:api for registration statistics
	 * 
	 * @return response with user statistics
	 */
	@GetMapping("/userstatistics")
	public ResponseEntity<HashMap<String, Long>> getStatistics() {
		HashMap<String, Long> userCount = dashboardService.getUserStatistics();
		return new ResponseEntity<HashMap<String, Long>>(userCount, HttpStatus.OK);

	}

	/**
	 * purpose:api to get latest registration statistic
	 * 
	 * @return response with sorted user
	 */
	@GetMapping("/sortuser")
	public ResponseEntity<List<User>> getLatestRegisteredUsers() {

		List<User> list = dashboardService.getLatestRegisteredUsers();
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return response with user registration statistics
	 */
	@GetMapping("/{year}/{month}")
	public ResponseEntity<Response> getYearData(@PathVariable int year, @PathVariable int month) {
		return new ResponseEntity<>(dashboardService.getUserStat(year, month), HttpStatus.OK);
	}
}
