package com.bridgelabz.usermanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.usermanagement.model.User;

/**
 * Purpose: Repository for user model class
 * 
 * @author pratiksha
 *
 */
public interface IRegisterRepository extends MongoRepository<User, String> {
	public User findByMobile(String mobile);

	public User findByEmailId(String emailId);
}
