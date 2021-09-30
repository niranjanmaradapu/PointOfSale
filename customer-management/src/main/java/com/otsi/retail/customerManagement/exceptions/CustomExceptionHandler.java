package com.otsi.retail.customerManagement.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otsi.retail.customerManagement.errors.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	private Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(404, "Record not found");
		log.error("error response is:"+error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(401, "Data not exists");
		log.error("error response is:"+error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<Object> invalidDataException(InvalidDataException emptyRecordException) {
		ErrorResponse<?> error = new ErrorResponse<>(403, "Please enter valid data");
		log.error("error response is:"+error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ServiceDownException.class)
	public ResponseEntity<Object> invalidDataException(ServiceDownException serviceDownException) {
		ErrorResponse<?> error = new ErrorResponse<>(406, "Internal calling service is down");
		log.error("error response is:"+error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	
	

}
