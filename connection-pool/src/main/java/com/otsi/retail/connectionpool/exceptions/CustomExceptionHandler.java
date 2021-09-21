package com.otsi.retail.connectionpool.exceptions;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otsi.retail.connectionpool.errors.ErrorResponse;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse error = new ErrorResponse(101, "record not found", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateRecordException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateRecordException(DuplicateRecordException duplicateRecordException) {
		ErrorResponse error = new ErrorResponse(103, " record already exists", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	


	@ExceptionHandler(value = EmptyInputException.class)
	public ResponseEntity<ErrorResponse> handleEmptyInputException(EmptyInputException emptyInputException) {
		ErrorResponse error = new ErrorResponse(500, " please enter some data", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException invalidDataException) {
		ErrorResponse error = new ErrorResponse(501, " please give valid data", new Date());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}


}

