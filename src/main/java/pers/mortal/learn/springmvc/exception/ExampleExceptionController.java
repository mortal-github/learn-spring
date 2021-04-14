package pers.mortal.learn.springmvc.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/exception")
public class ExampleExceptionController {

    @RequestMapping(method = RequestMethod.GET)
    public String throwException(){
        throw new ExampleSpringMVCException();
    }

//    @ExceptionHandler(ExampleSpringMVCException.class)
//    public String handleSpringMVCException(){
//        return "error";
//    }
}

