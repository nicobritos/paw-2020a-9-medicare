package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandingController extends GenericController {
    @RequestMapping("/")
    public ModelAndView landingPage(){
        return new ModelAndView("landing");
    }
}
