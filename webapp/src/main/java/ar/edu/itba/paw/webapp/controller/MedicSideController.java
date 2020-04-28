package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MedicSideController extends GenericController {
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
        mav.addObject("appointments", appointmentService.findPending(staff)); // TODO: cambiar
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());
        mav.setViewName("homeMedico");
        return mav;
    }
}
