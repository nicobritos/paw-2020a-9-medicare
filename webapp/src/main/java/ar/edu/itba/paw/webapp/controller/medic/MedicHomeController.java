package ar.edu.itba.paw.webapp.controller.medic;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.events.events.AppointmentCancelEvent;
import ar.edu.itba.paw.webapp.events.events.UserConfirmationTokenGenerationEvent;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import ar.edu.itba.paw.webapp.form.WorkdayForm;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class MedicHomeController extends GenericController {
    @Autowired
    UserService userService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    StaffSpecialtyService staffSpecialtyService;
    @Autowired
    StaffService staffService;
    @Autowired
    PatientService patientService;
    @Autowired
    LocalityService localityService;
    @Autowired
    OfficeService officeService;
    @Autowired
    WorkdayService workdayService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping("/staff/home")
    public ModelAndView home(@RequestParam(defaultValue = "0") String week, @RequestParam(required = false, name = "today") String newToday, HttpServletRequest request) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();

        List<Staff> userStaffs = this.staffService.findByUser(user.get());
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime monday;
        LocalDateTime selected = today;
        boolean isToday = true;
        if (newToday != null) {
            try {
                selected = LocalDateTime.parse(newToday);
                isToday = false;
            } catch (DateTimeParseException e) {

            }
        }
        if (week != null) {
            try {
                int weekOffset = Integer.parseInt(week);
                today = today.plusWeeks(weekOffset);
            } catch (NumberFormatException e) {

            }
        }
        today = today.plusDays(selected.getDayOfWeek() - today.getDayOfWeek());
        monday = today.minusDays(today.getDayOfWeek() - 1);

        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.addObject("today", today);
        mav.addObject("isToday", isToday);
        mav.addObject("monday", monday);
        mav.addObject("todayAppointments", this.appointmentService.findToday(userStaffs));

        List<Appointment> appointments;
        if (monday.isAfter(LocalDateTime.now())) {
            appointments = this.appointmentService.findByStaffsAndDay(userStaffs, LocalDateTime.now(), monday.plusDays(7));
        } else {
            appointments = this.appointmentService.findByStaffsAndDay(userStaffs, monday, monday.plusDays(7));
        }
        List<List<Appointment>> weekAppointments = new LinkedList<>();
        for (int i = 0; i <= 7; i++) {
            weekAppointments.add(new LinkedList<>());
        }

        for (Appointment appointment : appointments) {
            if (appointment.getFromDate().getDayOfWeek() < 1 || appointment.getFromDate().getDayOfWeek() > 7) {
                weekAppointments.get(0).add(appointment);
            } else {
                weekAppointments.get(appointment.getFromDate().getDayOfWeek()).add(appointment);
            }
        }
        String query = request.getQueryString();
        if (query != null && !query.isEmpty()) {
            query = "?" + query;
        }
        mav.addObject("query", query);
        mav.addObject("weekAppointments", weekAppointments); // lista de turnos que se muestra en la agenda semanal
        mav.addObject("specialties", this.staffSpecialtyService.list());
        mav.addObject("localities", this.localityService.list());
        mav.setViewName("medic/home");
        return mav;
    }

    @RequestMapping(value = "/staff/profile", method = RequestMethod.GET)
    public ModelAndView profile(@ModelAttribute("medicProfileForm") final UserProfileForm form) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
            mav.addObject("workdays", this.workdayService.findByUser(user.get()));
        }
        mav.addObject("verified", user.get().getVerified());
        mav.setViewName("medic/profile");
        return mav;
    }

    @RequestMapping(value = "/staff/profile", method = RequestMethod.POST)
    public ModelAndView editProfile(@Valid @ModelAttribute("medicProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        if (errors.hasErrors()) {
            return this.profile(form);
        }
        if ((!form.getPassword().isEmpty() && form.getPassword().length() < 8) || !form.getPassword().equals(form.getRepeatPassword())) {
            errors.reject("Min.medicProfileForm.password", new Object[]{8}, "Error");
            return this.profile(form);
        }
        Optional<User> userOptional = this.userService.findByUsername(form.getEmail());
        if (userOptional.isPresent() && !userOptional.get().equals(user.get())) { // si se edito el email pero ya existe cuenta con ese email
            errors.reject("AlreadyExists.medicProfileForm.email", null, "Error");
            return this.profile(form);
        }

        User editedUser = user.get();
        editedUser.setFirstName(form.getFirstName());
        editedUser.setSurname(form.getSurname());
        editedUser.setEmail(form.getEmail());
        editedUser.setPhone(form.getPhone());
        if (!form.getPassword().isEmpty())
            editedUser.setPassword(form.getPassword());
        this.userService.update(editedUser);
        this.createConfirmationEvent(request, editedUser);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.setViewName("redirect:/staff/profile");
        return mav;
    }

    @RequestMapping(value = "/staff/profile/workday", method = RequestMethod.GET)
    public ModelAndView addWorkday(@ModelAttribute("workdayForm") final WorkdayForm form) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.setViewName("medic/addWorkday");
        return mav;
    }

    @RequestMapping(value = "/staff/profile/workday", method = RequestMethod.POST)
    public ModelAndView addWorkdayAction(@Valid @ModelAttribute("workdayForm") final WorkdayForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        if (errors.hasErrors()) {
            return this.addWorkday(form);
        }


        Optional<Office> office = this.officeService.findById(form.getOfficeId());
        if (!office.isPresent()) {
            errors.reject("NotFound.workdayForm.office", null, "Error");
            return this.addWorkday(form);
        }

        Optional<Staff> realStaff = this.staffService.findByUser(user.get()).stream().filter(staff -> staff.getOffice().equals(office.get())).findAny();
        if (!realStaff.isPresent()) {
            errors.reject("NotFound.workdayForm.staff", null, "Error");
            return this.addWorkday(form);
        }
        String[] startTime = form.getStartHour().split(":");
        String[] endTime = form.getEndHour().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int endHour = Integer.parseInt(endTime[0]);
        int startMin = Integer.parseInt(startTime[1]);
        int endMin = Integer.parseInt(endTime[1]);

        if (startHour > endHour) {
            errors.reject("Invalid.workdayForm.workhours", null, "Error");
            return this.addWorkday(form);
        } else if ((startHour == endHour) && (startMin >= endMin)) {
            errors.reject("Invalid.workdayForm.workhours", null, "Error");
            return this.addWorkday(form);
        }

        Workday workday = new Workday();
        switch (form.getDow()) {
            case 7:
                workday.setDay(WorkdayDay.SUNDAY);
                break;
            case 1:
                workday.setDay(WorkdayDay.MONDAY);
                break;
            case 2:
                workday.setDay(WorkdayDay.TUESDAY);
                break;
            case 3:
                workday.setDay(WorkdayDay.WEDNESDAY);
                break;
            case 4:
                workday.setDay(WorkdayDay.THURSDAY);
                break;
            case 5:
                workday.setDay(WorkdayDay.FRIDAY);
                break;
            case 6:
                workday.setDay(WorkdayDay.SATURDAY);
                break;
            default:
                return this.addWorkday(form);
        }
        workday.setStartHour(startHour);
        workday.setStartMinute(startMin);
        workday.setEndHour(endHour);
        workday.setEndMinute(endMin);
        workday.setStaff(realStaff.get());
        workday = this.workdayService.create(workday);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.setViewName("redirect:/staff/profile");
        return mav;
    }

    @RequestMapping(value = "/staff/profile/workday/delete/{workdayId}", method = RequestMethod.POST)
    public ModelAndView deleteWorkday(@PathVariable("workdayId") final int workdayId) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Workday> workday = this.workdayService.findById(workdayId);
        if (!workday.isPresent() || !workday.get().getStaff().getUser().equals(user.get())) {
            return new ModelAndView("redirect:/staff/profile");
        }

        this.workdayService.remove(workdayId);

        return new ModelAndView("redirect:/staff/profile");
    }

    @RequestMapping(value = "/staff/appointment/{id}", method = RequestMethod.POST)
    public ModelAndView cancelAppointment(@PathVariable Integer id, HttpServletRequest request, @RequestParam(defaultValue = "0") String week, @RequestParam(required = false, name = "today") String newToday) {
        //get current user, check for null
        String query;
        if (newToday != null) {
            query = "?today=" + newToday + "&week=" + week;
        } else {
            query = "?week=" + week;
        }
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        //get staff for current user
        List<Staff> staff = this.staffService.findByUser(user.get());
        //get appointment to delete, check for "null"
        Optional<Appointment> appointment = this.appointmentService.findById(id);
        if (!appointment.isPresent()) {
            return new ModelAndView("redirect:/staff/home" + query);
        }
        //check if user is allowed to cancel
        boolean isAllowed = false;
        for (Staff s : staff) {
            if (s.equals(appointment.get().getStaff())) {
                isAllowed = true;
                break;
            }
        }
        //return response code for not allow
        if (!isAllowed) {
            return new ModelAndView("redirect:/staff/home" + query);
        }
        //cancel appointment
        this.appointmentService.remove(appointment.get()); // TODO
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(new AppointmentCancelEvent(user.get(), true, appointment.get().getPatient().getUser(), appointment.get(), request.getLocale(), baseUrl.toString()));
//        this.appointmentService.setStatus(appointment.get(), AppointmentStatus.CANCELLED);
        //return success
        return new ModelAndView("redirect:/staff/home" + query);
    }

    private void createConfirmationEvent(HttpServletRequest request, User user) {
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getRequestURI()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(
                new UserConfirmationTokenGenerationEvent(
                        baseUrl.toString(),
                        user,
                        request.getContextPath() + "/verifyEmail",
                        request.getLocale()
                )
        );
    }
}
