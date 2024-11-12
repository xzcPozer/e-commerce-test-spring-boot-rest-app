package com.dailycodework.dreamshops.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // обрабатывает все контроллеры
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class) // вызывается метод, когда перехватано исключение
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex){
        String message = "You dont have permission to this action";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }
}
