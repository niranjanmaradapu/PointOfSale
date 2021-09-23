package com.otsi.retail.userStore.exceptions;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otsi.retail.userStore.errors.ErrorResponse;



@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(404, "record not found");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotException) {

		ErrorResponse<?> error = new ErrorResponse<>(401, "data not found");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	

	@ExceptionHandler(value = DuplicateRecordException.class)
	public ResponseEntity<Object> handleDuplicateRecordException(DuplicateRecordException duplicateRecordException) {
		ErrorResponse<?> error = new ErrorResponse<>(402, "duplicate Record");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	
}
