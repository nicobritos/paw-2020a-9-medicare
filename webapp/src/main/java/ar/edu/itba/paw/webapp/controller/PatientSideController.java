package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PatientSideController extends GenericController {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    StaffSpecialtyService staffSpecialtyService;

    @Autowired
    LocalityService localityService;

    @Autowired
    UserService userService;


    @RequestMapping("/patient/home")
    public ModelAndView patientHome(){
        Optional<User> user = this.getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();

        Patient patient = new Patient();
        patient.setUser(user.get());

        mav.addObject("user", user);
        mav.addObject("appointments", appointmentService.findPending(patient));
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());

        mav.setViewName("homePaciente");
        return mav;
    }

    @RequestMapping(value = "/patient/profile", method = RequestMethod.GET)
    public ModelAndView patientProfile(@ModelAttribute("patientProfileForm") final UserProfileForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);

        mav.setViewName("patientProfile");
        return mav;
    }

    @RequestMapping(value="/patient/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("patientProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        if (errors.hasErrors()) {
            return this.patientProfile(form);
        }

        User editedUser = user.get();
        editedUser.setFirstName(form.getFirstName());
        editedUser.setSurname(form.getSurname());
        editedUser.setEmail(form.getEmail());
        //TODO: PHONE
        userService.update(editedUser);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("patientProfile");
        return mav;
    }
}
