package com.bridgelabz.usermanagement.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.bridgelabz.usermanagement.dto.RegisterDTO;
import com.bridgelabz.usermanagement.exception.LoginException;
import com.bridgelabz.usermanagement.exception.RegistrationException;
import com.bridgelabz.usermanagement.exception.UnautorizedException;
import com.bridgelabz.usermanagement.model.User;
import com.bridgelabz.usermanagement.repository.IRegisterRepository;
import com.bridgelabz.usermanagement.response.Response;
import com.bridgelabz.usermanagement.util.TokenUtil;
import com.bridgelabz.usermanagement.util.Utility;

/**
 * purpose:Service implementation for user controller
 * 
 * @author pratiksha
 *
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IRegisterRepository regRepository;

	@Autowired(required = true)
	private JavaMailSender javaMailSender;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * @param email
	 * @param password
	 * @return response with httpstatus
	 */
	public Response validateCredentials(String email, String password) {
		if (email.isEmpty() || password.isEmpty())
			throw new LoginException("Please enter both fields!!");
		User user = regRepository.findByEmailId(email);
		if (user == null)
			throw new LoginException("Invalid EmailId");
		boolean result = bCryptPasswordEncoder.matches(password, user.getPassword());

		if (result) {
			user.setOnline(true);
			return new Response(HttpStatus.OK, null, "Login sucess");
		}
		throw new UnautorizedException("Unauthorized User");
	}

	/**
	 * @param regdto
	 * @return response with httpstatus
	 */
	public Response registerUser(RegisterDTO regdto) {
		if (regRepository.findByEmailId(regdto.getEmailId()) != null) {
			throw new RegistrationException("EmailId already exist!!");
		}
		if (regRepository.findByMobile(regdto.getMobile()) != null) {
			throw new RegistrationException("Mobile number already exist!!");
		}
		User regUser = modelMapper.map(regdto, User.class);

		regUser.setPassword(bCryptPasswordEncoder.encode(regdto.getPassword()));
		sendEmail(regdto.getEmailId(), TokenUtil.getJWTToken(regdto.getEmailId()));
		regUser.setRegisteredDate(LocalDate.now());
		givepermissions(regUser);
		regRepository.save(regUser);
		verifyUser(regUser.getEmailId());
		return new Response(HttpStatus.OK, null, "success");
	}

	/**
	 * @param email
	 * @param token
	 * @return response with httpstatus
	 */
	public Response sendEmail(String email, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("pratikshatamadalge21@gmail.com");
		message.setTo(email);
		message.setSubject("Reset password Varification");
		message.setText("Verification token: " + token);
		javaMailSender.send(message);
		System.out.println("mail>>>> " + message);
		return new Response(HttpStatus.OK, null, "Mail sent successfully...");
	}

	/**
	 * @param email
	 * @return response with httpstatus
	 */
	@Override
	public String getJWTToken(String email) {
		return TokenUtil.getJWTToken(email);
	}

	/**
	 * @param file
	 * @param emailId
	 * @return response with httpstatus
	 * @throws Exception
	 */
	@Override
	public Response saveProfilePic(MultipartFile file, String emailId) throws Exception {
		User user = regRepository.findByEmailId(emailId);
		if (user == null)
			throw new UnautorizedException("Unautorized User");

		byte[] bytes = file.getBytes();
		String extension = file.getContentType().replace("image/", "");
		String fileLocation = Utility.PROFILE_PIC_LOCATION + emailId + "." + extension;
		Path path = Paths.get(fileLocation);
		Files.write(path, bytes);

		user.setProfilePic(fileLocation);
		regRepository.save(user);
		return new Response(HttpStatus.OK, null, Utility.RECORD_UPDATED);
	}

	/**
	 * @return list of user
	 */
	@Override
	public List<User> getUsers() {
		return regRepository.findAll();
	}

	/**
	 * @param emailId
	 * @return response with httpstatus
	 */
	@Override
	public Response deleteProfilePic(String emailId) {
		User register = regRepository.findByEmailId(emailId);
		if (register == null)
			throw new UnautorizedException("Unautorized User");

		String fileLocation = register.getProfilePic();
		File file = new File(fileLocation);
		file.delete();
		register.setProfilePic("");
		regRepository.save(register);
		return new Response(HttpStatus.OK, null, Utility.RECORD_UPDATED);
	}

	/**
	 * @param file
	 * @param emailId
	 * @return response with httpstatus
	 * @throws IOException
	 */
	public Response updateProfilePic(MultipartFile file, String emailId) throws IOException {
		User register = regRepository.findByEmailId(emailId);
		if (register == null)
			throw new UnautorizedException("Unautorized User");
		byte[] bytes = file.getBytes();
		String extension = file.getContentType().replace("image/", "");
		String fileLocation = Utility.PROFILE_PIC_LOCATION + emailId + "." + extension;
		Path path = Paths.get(fileLocation);
		Files.write(path, bytes);
		register.setProfilePic(fileLocation);
		regRepository.save(register);
		return new Response(HttpStatus.OK, null, Utility.RECORD_UPDATED);
	}

	/**
	 * @param token
	 * @return response with httpstatus
	 */
	@Override
	public Response verifyUser(String email) {
		User user = regRepository.findByEmailId(email);
		if (user == null)
			throw new UnautorizedException("Unautorized user");
		user.setVerified(true);
		regRepository.save(user);
		return new Response(HttpStatus.OK, null, Utility.RECORD_UPDATED);
	}

	/**
	 * purpose: method to get login history of a registered user
	 */
	@Override
	public ArrayList<Date> loginHistory(String email) {
		User user = regRepository.findByEmailId(email);
		ArrayList<Date> loginHistory = user.getLoginHistoty();
		return loginHistory;
	}

	/**
	 * purpose: logout method
	 */
	@Override
	public Response logout(String email) {
		User user = regRepository.findByEmailId(email);
		System.out.println("user details " + user);
		if (user.isOnline()) {
			user.setOnline(false);
			regRepository.save(user);
			return new Response(HttpStatus.OK, null, Utility.LOGOUTSUCCESS);
		}
		return new Response(HttpStatus.INTERNAL_SERVER_ERROR, null, Utility.LOGOUTFAILURE);
	}

	/**
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void givepermissions(User user) {
		if (user.getUserRole().contains("user")) {
			user.getPermissions().put("Dashboard", new HashMap() {
				{
					put("Add", false);
				}
				{
					put("Delete", false);
				}
				{
					put("modify", false);
				}
				{
					put("read", false);
				}
			});
			user.getPermissions().put("Settings", new HashMap() {
				{
					put("Add", false);
				}
				{
					put("Delete", false);
				}
				{
					put("modify", false);
				}
				{
					put("read", false);
				}
			});
			user.getPermissions().put("User Information", new HashMap() {
				{
					put("Add", false);
				}
				{
					put("Delete", false);
				}
				{
					put("modify", true);
				}
				{
					put("read", false);
				}
			});
			user.getPermissions().put("Web page 1", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", false);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("Web page 2", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", false);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("Web page 3", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", false);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});

		} else {
			user.getPermissions().put("Dashboard", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", true);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("Settings", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", true);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("User Information", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", true);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("Web page 1", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", true);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("Web page 2", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", true);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});
			user.getPermissions().put("Web page 3", new HashMap() {
				{
					put("Add", true);
				}
				{
					put("Delete", true);
				}
				{
					put("modify", true);
				}
				{
					put("read", true);
				}
			});

		}

	}
}
