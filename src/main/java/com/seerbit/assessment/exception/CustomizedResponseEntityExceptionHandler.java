package com.seerbit.assessment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        String message = ex.getLocalizedMessage();
        log.error(ex.getMessage());
        return buildResponseEntity(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatch(Exception ex, WebRequest request) {
        log.error("MethodArgumentTypeMismatchException, MethodArgumentNotValidException.class => ",ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(String apiResponse) {
        return new ResponseEntity<>(getError(apiResponse), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Map<String, Object> getError(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", false);
        response.put("timestamp", new Date());
        response.put("data", null);

        return response;
    }

}
