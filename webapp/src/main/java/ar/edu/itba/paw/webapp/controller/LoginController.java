package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.form.UserLoginForm;
import ar.edu.itba.paw.webapp.form.UserSignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController extends GenericController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView signupAction(@Valid @ModelAttribute("signupForm") final UserSignUpForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return this.signupIndex(form);
        }
        if (this.userService.findByUsername(form.getEmail()).isPresent()) {
            return this.signupIndex(form);
            // TODO
        }

        User newUser = this.userService.create(form.getAsUser());
        this.authenticateUserAndSetSession(newUser, form.getPassword(), request);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginAction(@Valid @ModelAttribute("loginForm") final UserLoginForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            // TODO:
            return this.loginIndex(form);
        }

        Optional<User> user = this.userService.findByUsername(form.getEmail());
        if(!user.isPresent()){ // email no existe
            // TODO: set errors
            return this.loginIndex(form);
        }

        this.authenticateUserAndSetSession(user.get(), form.getPassword(), request);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginIndex(@ModelAttribute("loginForm") final UserLoginForm form) {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signupIndex(@ModelAttribute("signupForm") final UserSignUpForm form) {
        return new ModelAndView("register");
    }


    @RequestMapping("/403")
    public ModelAndView forbidden(){
        return new ModelAndView("403");
    }

    private void authenticateUserAndSetSession(User user, String password, HttpServletRequest request) {
        try {
            String username = user.getEmail();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            // generate session if one doesn't exist
            request.getSession();

            Authentication authenticatedUser = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
