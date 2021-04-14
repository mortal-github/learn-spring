package pers.mortal.learn.springmvc.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExampleControllerAdvice {

    @ExceptionHandler(ExampleSpringMVCException.class)
    public String handleException(){
        return "error";
    }
}
