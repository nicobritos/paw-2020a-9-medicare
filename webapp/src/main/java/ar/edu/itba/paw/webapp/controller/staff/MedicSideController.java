package ar.edu.itba.paw.webapp.controller.staff;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import ar.edu.itba.paw.webapp.form.WorkdayForm;
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
public class MedicSideController extends GenericController {
    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    StaffSpecialtyService staffSpecialtyService;

    @Autowired
    StaffService staffService;

    @Autowired
    LocalityService localityService;

    @Autowired
    OfficeService officeService;

    @Autowired
    WorkdayService workdayService;

    @RequestMapping("/staff/home")
    public ModelAndView medicHome(){
        Optional<User> user = this.getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }
        ModelAndView mav = new ModelAndView();

        Staff staff = new Staff();

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
        mav.addObject("todayAppointments", appointmentService.findToday(staff));
        mav.addObject("appointments", appointmentService.find(staff)); // TODO: cambiar
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());
        mav.setViewName("medicSide/homeMedico");
        return mav;
    }

    @RequestMapping(value = "/staff/profile", method = RequestMethod.GET)
    public ModelAndView medicProfile(@ModelAttribute("medicProfileForm") final UserProfileForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);

        mav.setViewName("medicSide/medicProfile");
        return mav;
    }

    @RequestMapping(value="/staff/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("medicProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }

        if (errors.hasErrors() || form.getPassword().length()<8 || !form.getPassword().equals(form.getRepeatPassword())) {
            return this.medicProfile(form);
        }
        Optional<User> userOptional = userService.findByUsername(form.getEmail());
        if(userOptional.isPresent() && !userOptional.get().equals(user.get())){ // si se edito el email pero ya existe cuenta con ese email
            return this.medicProfile(form);
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
        mav.setViewName("medicSide/medicProfile");
        return mav;
    }

    @RequestMapping(value="/staff/profile/workday", method = RequestMethod.GET)
    public ModelAndView addWorkday(@ModelAttribute("workdayForm") final WorkdayForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("medicSide/addTurno");
        return mav;
    }

    @RequestMapping(value="/staff/profile/workday", method = RequestMethod.POST)
    public ModelAndView addWorkdayAction(@Valid @ModelAttribute("workdayForm") final WorkdayForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }

        if (errors.hasErrors()) {
            return this.addWorkday(form);
        }


        Optional<Office> office = officeService.findById(form.getOfficeId());
        if(!office.isPresent()){
            return this.addWorkday(form);
        }

        Optional<Staff> realStaff = user.get().getStaffs().stream().filter(staff -> staff.getOffice().equals(office.get())).findFirst();
        if(!realStaff.isPresent()){
            return this.addWorkday(form);
        }

        Workday workday = new Workday();
        switch (form.getDow()){
            case 0:
                workday.setDay(WorkdayDay.SUNDAY.name());
                break;
            case 1:
                workday.setDay(WorkdayDay.MONDAY.name());
                break;
            case 2:
                workday.setDay(WorkdayDay.TUESDAY.name());
                break;
            case 3:
                workday.setDay(WorkdayDay.WEDNESDAY.name());
                break;
            case 4:
                workday.setDay(WorkdayDay.THURSDAY.name());
                break;
            case 5:
                workday.setDay(WorkdayDay.FRIDAY.name());
                break;
            case 6:
                workday.setDay(WorkdayDay.SATURDAY.name());
                break;
            default:
                return this.addWorkday(form);
        }
        String[] startTime = form.getStartHour().split(":");
        workday.setStartHour(Integer.parseInt(startTime[0]));
        workday.setStartMinute(Integer.parseInt(startTime[1]));
        String[] endTime = form.getEndHour().split(":");
        workday.setEndHour(Integer.parseInt(endTime[0]));
        workday.setEndMinute(Integer.parseInt(endTime[1]));
        workday.setStaff(realStaff.get());
        workday = workdayService.create(workday);

        realStaff.get().getWorkdays().add(workday);
        staffService.update(realStaff.get());

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("redirect:/staff/profile");
        return mav;
    }

    @RequestMapping(value="/staff/profile/workday/delete/{workdayId}", method = RequestMethod.GET)
    public ModelAndView deleteWorkday(@PathVariable("workdayId") final int workdayId){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }
        Optional<Workday> workday = workdayService.findById(workdayId);
        if(!workday.isPresent() || !workday.get().getStaff().getUser().equals(user.get())){
            return new ModelAndView("error/403"); //todo: throw status code instead of this
        }

        workdayService.remove(workdayId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("redirect:/staff/profile");
        return mav;
    }
}
