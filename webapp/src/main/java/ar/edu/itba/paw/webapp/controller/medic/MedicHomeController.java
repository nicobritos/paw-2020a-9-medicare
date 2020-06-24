package ar.edu.itba.paw.webapp.controller.medic;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.events.events.AppointmentCancelEvent;
import ar.edu.itba.paw.webapp.events.events.UserConfirmationTokenGenerationEvent;
import ar.edu.itba.paw.webapp.form.SpecialtyForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import ar.edu.itba.paw.webapp.form.WorkdayForm;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
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
import java.util.*;

import static org.joda.time.DateTimeConstants.MONDAY;

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
    public ModelAndView home(@RequestParam(defaultValue = "0") String week, @RequestParam(required = false, name = "today") String selectedDay, HttpServletRequest request) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView();

        List<Staff> userStaffs = this.staffService.findByUser(user.get());
        LocalDate today = LocalDate.now();
        LocalDate monday;
        LocalDate selected = today;
        if (selectedDay != null) {
            try {
                selected = LocalDate.parse(selectedDay);
            } catch (DateTimeParseException ignored) { // Queda en selected = today
            }
        }
        if (week != null) {
            try {
                int weekOffset = Integer.parseInt(week);
                today = today.plusWeeks(weekOffset);
            } catch (NumberFormatException ignored) { // Queda en today
            }
        }
        today = today.plusDays(selected.getDayOfWeek() - today.getDayOfWeek());
        monday = today.minusDays(today.getDayOfWeek() - MONDAY);

        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.addObject("today", today);
        mav.addObject("monday", monday);
        mav.addObject("todayAppointments", this.appointmentService.findToday(userStaffs));
        List<List<Appointment>> weekAppointments = this.appointmentService.findByStaffsAndDay(userStaffs, monday.toLocalDateTime(new LocalTime(0,0,0)), monday.plusDays(7).toLocalDateTime(new LocalTime(0,0,0)));

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
        List<Workday> workdays = this.workdayService.findByUser(user.get());
        Map<Workday, Integer> appointmentMap = new HashMap<>();
        for(Workday workday: workdays){
            List<Appointment> appointments = this.appointmentService.findByWorkday(workday);
            List<Appointment> myAppts = new LinkedList<>();
            for(Appointment appointment : appointments){
                if(appointment.getStaff().getUser().equals(user.get())){
                    myAppts.add(appointment);
                }
            }
            appointmentMap.put(workday, myAppts.size());
        }
        if (this.isStaff()) {
            List<Staff> staffs = this.staffService.findByUser(user.get());
            mav.addObject("staffs", staffs);
            if(!staffs.isEmpty()){
                mav.addObject("specialties", staffs.get(0).getStaffSpecialties()); // Todos los staffSpecialties tienen el mismo stafs
            } else {
                mav.addObject("specialties", Collections.emptyList());
            }
            mav.addObject("workdays", workdays);
            mav.addObject("appointmentMap", appointmentMap);
        }
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
        Optional<User> newEmailUser = this.userService.findByUsername(form.getEmail());
        if (newEmailUser.isPresent() && !newEmailUser.get().equals(user.get())) { // si se edito el email pero ya existe cuenta con ese email
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
        String[] startTime = form.getStartHour().split(":"); // Formato: "HH:MM"
        String[] endTime = form.getEndHour().split(":"); // Formato: "HH:MM"
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

        boolean addedDay = false;
        for(int i=0;i<7 && i<form.getDow().length;i++){
            if(form.getDow()[i]){
                addedDay = true;
                Workday workday = new Workday();
                workday.setDay(WorkdayDay.from(i+1));
                if(workday.getDay() == null){
                    return this.addWorkday(form);
                }
                workday.setStartHour(startHour);
                workday.setStartMinute(startMin);
                workday.setEndHour(endHour);
                workday.setEndMinute(endMin);
                workday.setStaff(realStaff.get());
                this.workdayService.create(workday);
            }
        }
        if(!addedDay){
            return this.addWorkday(form);
        }

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

    @RequestMapping(value = "/staff/appointment/workday/{workdayId}", method = RequestMethod.POST)
    public ModelAndView cancelAppointments(@PathVariable("workdayId") final int workdayId) {
        //get current user, check for null
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        //get staff for current user
        List<Staff> staff = this.staffService.findByUser(user.get()); // TODO: add staff list inside User model
        //get appointment to delete, check for "null"
        Optional<Workday> workday = this.workdayService.findById(workdayId);
        if(!workday.isPresent()){
            return new ModelAndView("redirect:/staff/profile");
        }
        List<Appointment> appointments = this.appointmentService.findByWorkday(workday.get());
        //check if user is allowed to cancel
        for(Appointment a: appointments) {
            boolean isAllowed = false;
            for (Staff s : staff) {
                if (s.equals(a.getStaff())) {
                    isAllowed = true;
                    break;
                }
            }
            if(isAllowed) {
                this.appointmentService.remove(a.getId()); // TODO: all the logic above should be done inside service
                //createCancelEvent(request, user.get(), a);
            }
        }
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
        List<Staff> staff = this.staffService.findByUser(user.get()); // TODO: add staff list inside User model
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
        this.appointmentService.remove(appointment.get().getId()); // TODO: all the logic above should be done inside service
        createCancelEvent(request, user.get(), appointment.get());
        return new ModelAndView("redirect:/staff/home" + query);
    }

    @RequestMapping(value = "/staff/profile/specialty", method = RequestMethod.POST)
    public ModelAndView addSpecialtyAction(@Valid @ModelAttribute("specialtyForm") final SpecialtyForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        if (errors.hasErrors()) {
            return this.addSpecialty(form);
        }

        ModelAndView mav = new ModelAndView();
        List<Staff> staffs = this.staffService.findByUser(user.get());
        Optional<StaffSpecialty> staffSpecialty = this.staffSpecialtyService.findById(form.getSpecialtyId());
        if(staffSpecialty.isPresent()) {
            for (Staff staff : staffs) {
                if(!staff.getStaffSpecialties().contains(staffSpecialty.get())) {
                    staff.getStaffSpecialties().add(staffSpecialty.get());
                    staffService.update(staff);
                }
            }
        }
        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.setViewName("redirect:/staff/profile");
        return mav;
    }

    @RequestMapping(value = "/staff/profile/specialty", method = RequestMethod.GET)
    public ModelAndView addSpecialty(@ModelAttribute("specialtyForm") final SpecialtyForm form) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if (this.isStaff()) {
            mav.addObject("staffs", this.staffService.findByUser(user.get()));
        }
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.setViewName("medic/addSpecialty");
        return mav;
    }

    @RequestMapping(value = "/staff/profile/specialty/delete/{specialtyId}", method = RequestMethod.POST)
    public ModelAndView deleteSpecialty(@PathVariable("specialtyId") final int specialtyId) {
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ModelAndView("redirect:/login");
        }
        List<Staff> staffs = this.staffService.findByUser(user.get());
        Optional<StaffSpecialty> staffSpecialty = this.staffSpecialtyService.findById(specialtyId);
        if(staffSpecialty.isPresent()) {
            for (Staff staff : staffs) {
                staff.getStaffSpecialties().remove(staffSpecialty.get());
                staffService.update(staff);
            }
        }

        return new ModelAndView("redirect:/staff/profile");
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

    private void createCancelEvent(HttpServletRequest request, User user, Appointment appointment) {
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(new AppointmentCancelEvent(user, true, appointment.getPatient().getUser(), appointment, request.getLocale(), baseUrl.toString()));
    }
}
