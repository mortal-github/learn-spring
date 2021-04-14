package pers.mortal.learn.springmvc.multipart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

import javax.servlet.http.Part;
import java.io.IOException;

@Controller
@RequestMapping("/multipart")
public class MultipartController {

    @RequestMapping(method = RequestMethod.GET)
    public String getForm(){
        return "multipartForm";
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public String postForm(@RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {
//
//        profilePicture.transferTo(new File(ExampleMultipartConfig.path + "/" + profilePicture.getOriginalFilename()));
//        return "redirect:/multipart";
//    }

    @RequestMapping(method = RequestMethod.POST)
    public String postForm(@RequestPart("profilePicture") Part profilePicture) throws IOException {

        profilePicture.write(ExampleMultipartConfig.path + "/" + profilePicture.getSubmittedFileName());
        return "redirect:/multipart";
    }

}