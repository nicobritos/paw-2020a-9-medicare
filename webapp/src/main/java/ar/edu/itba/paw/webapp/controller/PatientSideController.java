package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
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

    @Autowired
    private StaffService staffService;


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
        mav.addObject("appointments", appointmentService.find(patient));
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());

        mav.setViewName("patientSide/homePaciente");
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

        mav.setViewName("patientSide/patientProfile");
        return mav;
    }

    @RequestMapping(value="/patient/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("patientProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        if (errors.hasErrors() || form.getPassword().length()<8 || !form.getPassword().equals(form.getRepeatPassword())) {
            return this.patientProfile(form);
        }
        Optional<User> userOptional = userService.findByUsername(form.getEmail());
        if(userOptional.isPresent() && !userOptional.get().equals(user.get())){ // si se edito el email pero ya existe cuenta con ese email
            return this.patientProfile(form);
        }

        User editedUser = user.get();
        editedUser.setFirstName(form.getFirstName());
        editedUser.setSurname(form.getSurname());
        editedUser.setEmail(form.getEmail());
        if(!form.getPassword().isEmpty())
            editedUser.setPassword(form.getPassword());
        //TODO: PHONE
        userService.update(editedUser);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("patientSide/patientProfile");
        return mav;
    }


    //TODO: FORMS
    @RequestMapping(value = "/patient/appointment/{staffId}/{year}/{dayOfYear}", method = RequestMethod.GET)
    public ModelAndView makeAppointment(@PathVariable("staffId") final int staffId ,@PathVariable("year") final int year, @PathVariable("dayOfYear") final int dayOfYear){
        ModelAndView mav = new ModelAndView();
        LocalDate date = LocalDate.ofYearDay(year, dayOfYear);
        if(date.isBefore(LocalDate.now())){
            return new ModelAndView("redirect:/appointment/" + staffId); //TODO: errors
        }
        Optional<Staff> staff = staffService.findById(staffId);
        if(!staff.isPresent()){
            return new ModelAndView("redirect:/mediclist/1"); //TODO: errors
        }
        mav.addObject("user", getUser());
        mav.addObject("staff", staff.get());
        mav.addObject("date", date);
        mav.setViewName("patientSide/reservarTurno");
        return mav;
    }
}
