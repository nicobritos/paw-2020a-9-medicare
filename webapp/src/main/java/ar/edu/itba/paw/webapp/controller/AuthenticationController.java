package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CountryService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.ProvinceService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.UserRole;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.controller.utils.JsonResponse;
import ar.edu.itba.paw.webapp.events.events.UserConfirmationTokenGenerationEvent;
import ar.edu.itba.paw.webapp.form.authentication.PatientSignUpForm;
import ar.edu.itba.paw.webapp.form.authentication.StaffSignUpForm;
import ar.edu.itba.paw.webapp.form.authentication.UserLoginForm;
import ar.edu.itba.paw.webapp.transformer.LocalityTransformer;
import ar.edu.itba.paw.webapp.transformer.ProvinceTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthenticationController extends GenericController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LocalityService localityService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private ProvinceTransformer provinceTransformer;
    @Autowired
    private LocalityTransformer localityTransformer;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginIndex(@ModelAttribute("loginForm") final UserLoginForm form, @RequestParam(value = "token", required = false) String token) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("token", token);
        mav.setViewName("authentication/login");
        return mav;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signupIndex() {
        return new ModelAndView("authentication/signup");
    }

    // Sign up del medico
    @RequestMapping(value = "/signup/staff", method = RequestMethod.GET)
    public ModelAndView signupStaffIndex(@ModelAttribute("signupForm") final StaffSignUpForm form) {
        Map<String, String> countryMap = this.countryService.list().stream().collect(Collectors.toMap(Country::getId, Country::getName));
        ModelAndView modelAndView = new ModelAndView("authentication/signupStaff");
        modelAndView.addObject("countryMap", countryMap);
        return modelAndView;
    }

    @RequestMapping(value = "/signup/staff", method = RequestMethod.POST)
    public ModelAndView signupStaffAction(@Valid @ModelAttribute("signupForm") final StaffSignUpForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return this.signupStaffIndex(form);
        }
        if (!form.getPassword().equals(form.getRepeatPassword())) {
            errors.reject("Equals.signupForm.repeatPassword", null, "Error");
            return this.signupStaffIndex(form);
        }
        Optional<Locality> locality = this.localityService.findById(form.getLocalityId());
        if (!locality.isPresent()) {
            errors.reject("NotNull.signupForm.localityId", null, "Error");
            return this.signupStaffIndex(form);
        }

        User newUser = form.getAsUser();

        Office office = new Office();
        office.setEmail(newUser.getEmail());
        office.setName("Consultorio de " + newUser.getFirstName() + " " + newUser.getSurname());
        office.setLocality(locality.get());
        office.setStreet(form.getAddress());
        try {
            newUser = this.userService.createAsStaff(newUser, office);
        } catch (EmailAlreadyExistsException e) {
            errors.reject("EmailAlreadyTaken.signupForm.email", null, "Error");
            return this.signupStaffIndex(form);
        }
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(new UserConfirmationTokenGenerationEvent(baseUrl.toString(), newUser, "/verifyEmail", request.getLocale()));
        this.authenticateSignedUpUser(form.getAsUser(), form.getPassword(), request);
        return new ModelAndView("redirect:/verifyEmail");
    }

    // Sign up del paciente
    @RequestMapping(value = "/signup/patient", method = RequestMethod.GET)
    public ModelAndView signupPatientIndex(@ModelAttribute("signupForm") final PatientSignUpForm form) {
        return new ModelAndView("authentication/signupPatient");
    }

    @RequestMapping(value = "/signup/patient", method = RequestMethod.POST)
    public ModelAndView signupPatientAction(@Valid @ModelAttribute("signupForm") final PatientSignUpForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return this.signupPatientIndex(form);
        }
        if (!form.getPassword().equals(form.getRepeatPassword())) {
            errors.reject("Equals.signupForm.repeatPassword", null, "Error");
            return this.signupPatientIndex(form);
        }

        User newUser;
        try {
            newUser = this.userService.create(form.getAsUser());
        } catch (EmailAlreadyExistsException e) {
            errors.reject("EmailAlreadyTaken.signupForm.email", null, "Error");
            return this.signupPatientIndex(form);
        }
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(new UserConfirmationTokenGenerationEvent(baseUrl.toString(), newUser, "/verifyEmail", request.getLocale()));
        this.authenticateSignedUpUser(form.getAsUser(), form.getPassword(), request);

        return new ModelAndView("redirect:/verifyEmail");
    }

    // Página en la que paran las cuentas sin verificacion del mail
    @RequestMapping(value = "/verifyEmail", method = RequestMethod.GET)
    public ModelAndView verifyEmail(@RequestParam(value = "token", required = false) String token, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Optional<User> userOptional = this.getUser();
        if (!userOptional.isPresent()) {
            mav.setViewName("redirect:/login?token=" + token);
            return mav;
        }
        if(userOptional.get().getVerified()){
            mav.setViewName("redirect:/home");
            return mav;
        }
        boolean tokenError;
        if (token == null) {
            tokenError = false;
        } else if (!this.userService.confirm(userOptional.get(), token)) {
            tokenError = true;
        } else {
            this.updateRole(userOptional.get());
            mav.addObject("user", userOptional);
            mav.setViewName("redirect:/home");
            return mav;
        }
        mav.addObject("user", this.getUser());
        mav.addObject("tokenError", tokenError);
        mav.setViewName("unverified");
        return mav;
    }


    // FUNCIONES AUXILIARES DEL SIGNUP DEL MEDICO
    @RequestMapping(value = "/signup/staff/provinces/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse getProvinces(@PathVariable String id) {
        return this.formatJsonResponse(() -> {
            Country country = new Country();
            country.setId(id);
            Collection<Province> provinces = this.provinceService.findByCountry(country);
            return this.provinceTransformer.transform(provinces);
        });
    }

    @RequestMapping(value = "/signup/staff/localities/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse getLocalities(@PathVariable Integer id) {
        return this.formatJsonResponse(() -> {
            Province province = new Province();
            province.setId(id);
            Collection<Locality> localities = this.localityService.findByProvince(province);
            return this.localityTransformer.transform(localities);
        });
    }


    // Funcion que autentifica al usuario
    private void authenticateSignedUpUser(User user, String password, HttpServletRequest request) {
        try {
            String username = user.getEmail();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(this.getRoles(user)));
            // generate session if one doesn't exist
            request.getSession();
            Authentication authenticatedUser;
            authenticatedUser = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        } catch (Exception e) {
            LOGGER.error("Error authenticating user {} after signup with stacktrace: \n{}", user.getEmail(), Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Devuelve una lista de roles en formato de csv con delimitador ','
     **/
    private String getRoles(User user) {
        String prefix = "ROLE_";
        if (!user.getVerified()) {
            return prefix + UserRole.UNVERIFIED;
        }
        if (this.userService.isStaff(user)) {
            return prefix + UserRole.STAFF;
        } else {
            return prefix + UserRole.PATIENT;
        }

    }

    private void updateRole(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(this.getRoles(user));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
