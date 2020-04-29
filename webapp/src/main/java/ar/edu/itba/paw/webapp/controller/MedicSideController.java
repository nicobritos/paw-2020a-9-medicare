package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class MedicSideController extends GenericController {
    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    StaffSpecialtyService staffSpecialtyService;

    @Autowired
    LocalityService localityService;

    @RequestMapping("/staff/home")
    public ModelAndView medicHome(){
        Optional<User> user = this.getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();

        Staff staff = new Staff();
        staff.setUser(user.get());

        LocalDate today = LocalDate.now();
        LocalDate monday;

        switch (LocalDate.now().getDayOfWeek()){
            case SUNDAY:
                monday = today.plusDays(1);
                break;
            case MONDAY:
                monday = today;
                break;
            case TUESDAY:
                monday = today.minusDays(1);
                break;
            case WEDNESDAY:
                monday = today.minusDays(2);
                break;
            case THURSDAY:
                monday = today.minusDays(3);
                break;
            case FRIDAY:
                monday = today.minusDays(4);
                break;
            case SATURDAY:
                monday = today.minusDays(5);
                break;
            default:
                throw new RuntimeException();
        }

        mav.addObject("user", user);
        mav.addObject("today", today);
        mav.addObject("monday", monday);
        mav.addObject("todayAppointments", appointmentService.findTodayAppointments(staff));
        mav.addObject("appointments", appointmentService.find(staff)); // TODO: cambiar
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());
        mav.setViewName("homeMedico");
        return mav;
    }

    @RequestMapping(value = "/staff/profile", method = RequestMethod.GET)
    public ModelAndView medicProfile(@ModelAttribute("medicProfileForm") final UserProfileForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);

        mav.setViewName("medicProfile");
        return mav;
    }

    @RequestMapping(value="/staff/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("medicProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        if (errors.hasErrors()) {
            return this.medicProfile(form);
        }

        User editedUser = user.get();
        editedUser.setFirstName(form.getFirstName());
        editedUser.setSurname(form.getSurname());
        editedUser.setEmail(form.getEmail());
        //TODO: PHONE
        userService.update(editedUser);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("medicProfile");
        return mav;
    }
}
