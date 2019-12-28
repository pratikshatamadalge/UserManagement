package com.bridgelabz.usermanagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author pratiksha
 *
 */
@Configuration
@EnableSwagger2
public class UserManagementConfig {

	/**
	 * Purpose to return ObjectMapper Bean
	 * 
	 * @return ObjectMapper Bean
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/**
	 * Purpose to return BCryptPasswordEncoder Bean
	 * 
	 * @return BCryptPasswordEncoder Bean
	 */
	@Bean
	public BCryptPasswordEncoder bcyBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Purpose to return Docket Bean to configure swagger
	 * 
	 * @return Docket Bean
	 */
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.bridgelabz.fundoo")).build();
	}

}
