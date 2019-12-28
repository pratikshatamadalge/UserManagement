package com.bridgelabz.usermanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.bridgelabz.usermanagement.dto.RegisterDTO;
import com.bridgelabz.usermanagement.model.User;
import com.bridgelabz.usermanagement.response.Response;

/**
 * @author pratiksha
 *
 */
public interface IUserService {

	Response validateCredentials(String email, String password);

	Response registerUser(RegisterDTO regdto);

	Response sendEmail(String email, String token);

	String getJWTToken(String email);

	Response saveProfilePic(MultipartFile file, String emailId) throws Exception;

	Response deleteProfilePic(String emailId);

	Response updateProfilePic(MultipartFile file, String emailId) throws IOException;

	List<User> getUsers();

	Response verifyUser(String token);

	ArrayList<Date> loginHistory(String email);

	Response logout(String email);
}
