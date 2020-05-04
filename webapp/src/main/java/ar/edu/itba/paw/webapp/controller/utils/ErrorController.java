package ar.edu.itba.paw.webapp.controller.utils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController extends GenericController {
    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return new ModelAndView("error/403");
    }

    @RequestMapping("/404")
    public ModelAndView notFound() {
        return new ModelAndView("error/404");
    }

    @RequestMapping("/500")
    public ModelAndView serverError() {
        return new ModelAndView("error/500");
    }
}
