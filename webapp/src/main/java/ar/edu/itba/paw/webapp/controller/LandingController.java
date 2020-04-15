package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandingController {
    @RequestMapping("/")
    public ModelAndView landingPage(){
        final ModelAndView mov = new ModelAndView("landing");
        return mov;
    }
}
