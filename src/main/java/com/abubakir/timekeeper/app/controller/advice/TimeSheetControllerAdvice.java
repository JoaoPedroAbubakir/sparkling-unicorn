package com.abubakir.timekeeper.app.controller.advice;

import com.abubakir.timekeeper.app.controller.TimeSheetController;
import com.abubakir.timekeeper.app.exception.InvalidDateTimeException;
import com.abubakir.timekeeper.app.exception.RequiredFieldNotPopulatedException;
import com.abubakir.timekeeper.app.response.ErrorResponse;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(assignableTypes = TimeSheetController.class)
public class TimeSheetControllerAdvice {

    @ExceptionHandler(InvalidDateTimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidDateTimeException(InvalidDateTimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequiredFieldNotPopulatedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidDateTimeException(RequiredFieldNotPopulatedException e) {
        ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
