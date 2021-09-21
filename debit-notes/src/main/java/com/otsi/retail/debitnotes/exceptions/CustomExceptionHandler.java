package com.otsi.retail.debitnotes.exceptions;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otsi.retail.debitnotes.errors.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse error = new ErrorResponse(101, "record not found", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEmptyInputException(DataNotFoundException dataNotFoundException) {
		ErrorResponse error = new ErrorResponse(102, " data not found", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
