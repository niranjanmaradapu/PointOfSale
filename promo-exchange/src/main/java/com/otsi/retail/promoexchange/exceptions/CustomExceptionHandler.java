package com.otsi.retail.promoexchange.exceptions;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.otsi.retail.promoexchange.errors.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	//private Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
	 
	private Logger log=LogManager.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(404, "Record not found");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(401, "Data not found");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateRecordFoundException.class)
	public ResponseEntity<Object> handleRecordFoundException(
			DuplicateRecordFoundException duplicateRecordFoundException) {
		ErrorResponse<?> error = new ErrorResponse<>(402, "Record already exists");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<Object> invalidDataException(InvalidDataException emptyRecordException) {
		ErrorResponse<?> error = new ErrorResponse<>(403, "Please enter valid data");
		log.error("error response is:" + error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

}
