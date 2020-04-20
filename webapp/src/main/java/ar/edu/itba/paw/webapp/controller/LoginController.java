package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.UserLoginForm;
import ar.edu.itba.paw.webapp.form.UserSignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signup", method = { RequestMethod.POST })
    public ModelAndView login(
            @Valid @ModelAttribute("signupForm") final UserSignUpForm form,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            // TODO
            throw new RuntimeException("ASd");
        }

        User newUser = this.userService.create(form.getAsUser());
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public ModelAndView login(
            @Valid @ModelAttribute("loginForm") final UserLoginForm form,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            // TODO
            throw new RuntimeException("ASd");
        }

        Optional<User> user = this.userService.login(form.getEmail(), form.getPassword());
        return new ModelAndView("index");
    }
}
