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
	public ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse error = new ErrorResponse(404, "Record not found", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException dataNotException) {
		ErrorResponse error = new ErrorResponse(102, "Data not found", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = DuplicateRecordFoundException.class)
	public ResponseEntity<ErrorResponse> handleRecordFoundException(DuplicateRecordFoundException recordFoundException) {
		ErrorResponse error = new ErrorResponse(400, "Record already exists", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<ErrorResponse> invalidDataException(InvalidDataException emptyRecordException) {
		ErrorResponse error = new ErrorResponse(500, "Please enter valid data", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
