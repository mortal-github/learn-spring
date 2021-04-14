package pers.mortal.learn.springmvc.redirect;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/redirect")
public class ExampleRedirectController {
    //使用URL模板
    @RequestMapping(value= "/url", method = RequestMethod.GET)
    public String transferByURLPattern(@RequestParam("data") String data,
                                       @RequestParam("id") int id,
                                       Model model){
        // 模型中的属性匹配重定向URL中的占位符，则自动填充到占位符，并对不安全字符进行转义。
        // 除此之外，模型中所有其他的原始类型值都可以添加到URL中作为查 询参数。
        // 即模型中的属性不匹配重定向URL中的任何占位符，则它会自动以查询参数的形式附加到重定向URL上。
        model.addAttribute("data", data);
        model.addAttribute("id", id);
        return "redirect:/redirect/url/{id}";
    }

    @RequestMapping(value="/url/{id}", method=RequestMethod.GET)
    public String redirectUrl(@RequestParam("data") String data,
                              @PathVariable("id") int id,
                              Model model){
        model.addAttribute("data",data);
        model.addAttribute("id", id);
        return "redirectPage";
    }


    //使用Flash属性
    @RequestMapping(value="/flash", method= RequestMethod.GET)
    public String transerByFlashAttribute(@RequestParam("data") String data,
                                          @RequestParam("id") int id,
                                          RedirectAttributes model){
        model.addFlashAttribute("data", data);
        model.addAttribute("id", id);

        return "redirect:/redirect/flash/{id}";
    }

    @RequestMapping(value="/flash/{id}", method = RequestMethod.GET)
    public String redirectFlash( @PathVariable("id") int id,
                                Model model){
        model.addAttribute("id", id);
        return "redirectPage";
    }
}
