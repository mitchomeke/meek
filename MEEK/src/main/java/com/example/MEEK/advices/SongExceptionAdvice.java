package com.example.MEEK.advices;


import com.example.MEEK.exceptions.SongNotFound;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SongExceptionAdvice {

    @ExceptionHandler(SongNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String songNotFound(SongNotFound ex){
        return ex.getMessage();
    }
}
