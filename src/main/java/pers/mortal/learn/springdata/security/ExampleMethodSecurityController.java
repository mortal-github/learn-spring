package pers.mortal.learn.springdata.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/method")
public class ExampleMethodSecurityController {
    @Autowired
    private ExampleMethodSecurityBean exampleMethodSecurityBean;

    @RequestMapping(value = "/Secured", method = RequestMethod.GET)
    public String testSecured(){
       exampleMethodSecurityBean.useSecured();
        return "redirect:/";
    }

    @RequestMapping(value = "/RolesAllowed",method = RequestMethod.GET)
    public String testRolesAllowed(){
        exampleMethodSecurityBean.useRolesAllowed();
        return "redirect:/";
    }

    @RequestMapping(value = "/Authorize", method = RequestMethod.GET)
    public String testAuthorize(@RequestParam("count") int count){
        exampleMethodSecurityBean.useAuthorize(count);
        return "redirect:/";
    }

    @RequestMapping(value = "/filter", method =  RequestMethod.GET)
    public String testFilter(@RequestParam("min") int min, @RequestParam("max") int max){
        List<Integer> list = IntStream.range(min, max).boxed().collect(Collectors.toList());

        List<Integer> result = exampleMethodSecurityBean.useFilter(list);

        result.toString();
        return "redirect:/";
    }

    @RequestMapping(value = "/permissionEvaluator", method = RequestMethod.GET)
    public String testPermissionEvaluator(@RequestParam("min") int min, @RequestParam("max") int max){
        List<Integer> list = IntStream.range(min, max).boxed().collect(Collectors.toList());

        List<Integer> result = exampleMethodSecurityBean.usePermissionEvaluator(list);

        result.toString();
        return "redirect:/";
    }
}
