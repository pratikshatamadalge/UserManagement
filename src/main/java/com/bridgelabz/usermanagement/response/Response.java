package com.bridgelabz.usermanagement.response;

import java.io.Serializable;

import org.apache.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private org.springframework.http.HttpStatus statusCode;
	private Object data;
	private String message;
}
