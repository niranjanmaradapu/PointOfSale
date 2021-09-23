package com.otsi.retail.newSale.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.otsi.retail.newSale.errors.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(404, "record not found");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(401, "data not found");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CustomerNotFoundExcecption.class)
	public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundExcecption customerNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(400, "customer not found");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateRecordException.class)
	public ResponseEntity<Object> handleDuplicateRecordException(DuplicateRecordException duplicateRecordException) {
		ErrorResponse<?> error = new ErrorResponse<>(402, "duplicate Record");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidInputException.class)
	public ResponseEntity<Object> handleInvalidInputException(InvalidInputException invalidInputException) {
		ErrorResponse<?> error = new ErrorResponse<>(405, "Invalid Input");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

}
