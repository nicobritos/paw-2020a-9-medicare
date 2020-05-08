package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
public class UnverifiedController extends GenericController {
    @RequestMapping("/unverified")
    public ModelAndView unverified(){
        final ModelAndView mov = new ModelAndView("unverified");
        Optional<User> user = getUser();
        if(!user.isPresent()){
            System.out.println("hola");
            return new ModelAndView("error/404");
        }
        mov.addObject("user",user);
        return mov;
    }
}
