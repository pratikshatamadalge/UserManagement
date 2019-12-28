package com.bridgelabz.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

		@ExceptionHandler(LoginException.class)
		public ResponseEntity<ErrorResponse>loginException(Exception ex)
		{
			return new ResponseEntity<>(new ErrorResponse(401,null,ex.getMessage()),HttpStatus.OK);
		}
		
		
		@ExceptionHandler(RegistrationException.class)
		public ResponseEntity<ErrorResponse>registrationException(Exception ex)
		{
			return new ResponseEntity<>(new ErrorResponse(400,null,ex.getMessage()),HttpStatus.OK);
		}
		
		
		@ExceptionHandler(RecordNotFoundException.class)
		public ResponseEntity<ErrorResponse>recordNotFound(Exception ex)
		{
			return new ResponseEntity<>(new ErrorResponse(404,null,ex.getMessage()),HttpStatus.OK);
		}
		
		@ExceptionHandler(UnautorizedException.class)
		public ResponseEntity<ErrorResponse>unautorizedException(Exception ex)
		{
			return new ResponseEntity<>(new ErrorResponse(400,null,ex.getMessage()),HttpStatus.OK);
		}
}
