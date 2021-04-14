package pers.mortal.learn.springmvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.NOT_FOUND
        , reason = "Spring MVC 异常"
)
public class ExampleSpringMVCException extends RuntimeException {
}
