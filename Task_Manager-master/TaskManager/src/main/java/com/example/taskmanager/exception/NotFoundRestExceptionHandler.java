package com.example.taskmanager.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundRestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotFoundRestExceptionHandler.class);
    //For Not Found Objects in DataBase
    @ExceptionHandler
    public ResponseEntity<NotFoundErrorResponse> handleException(NotFoundException exc){

        NotFoundErrorResponse error = new NotFoundErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        LOGGER.error("An error occurred while looking for the object", exc);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }


    //add another generic exception handler for other exceptions that may happen (Unspecified ones) error code = 500
    @ExceptionHandler
    public ResponseEntity<NotFoundErrorResponse> handleException(Exception exc){


        NotFoundErrorResponse error = new NotFoundErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        LOGGER.error("An error occurred ", exc);
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
