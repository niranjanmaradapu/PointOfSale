package com.otsi.retail.promoexchange.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otsi.retail.promoexchange.errors.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(404, "Record not found");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(401, "Data not found");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(value = DuplicateRecordFoundException.class)
	public ResponseEntity<Object> handleRecordFoundException(DuplicateRecordFoundException duplicateRecordFoundException) {
		ErrorResponse<?> error = new ErrorResponse<>(402, "Record already exists");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<Object> invalidDataException(InvalidDataException emptyRecordException) {
		ErrorResponse<?> error = new ErrorResponse<>(403, "Please enter valid data");
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

}
