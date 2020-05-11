package ar.edu.itba.paw.webapp.controller.staff;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.controller.utils.JsonResponse;
import ar.edu.itba.paw.webapp.events.AppointmentCancelEvent;
import ar.edu.itba.paw.webapp.events.UserConfirmationTokenGenerationEvent;
import ar.edu.itba.paw.webapp.exceptions.UnAuthorizedAccessException;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import ar.edu.itba.paw.webapp.form.WorkdayForm;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ModelAndView medicHome(@RequestParam(defaultValue = "0") String week,@RequestParam(required = false, name = "today") String newToday){
        Optional<User> user = this.getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }
        ModelAndView mav = new ModelAndView();

        List<Staff> userStaffs = staffService.findByUser(user.get().getId());
        DateTime today = DateTime.now();
        DateTime monday;
        DateTime selected = today;
        boolean isToday=true;
        if(newToday!=null){
            try{
                selected = DateTime.parse(newToday);
                isToday = false;
            }catch (DateTimeParseException e){

            }
        }
        if(week!=null){
            try{
                int weekOffset = Integer.parseInt(week);
                today = today.plusWeeks(weekOffset);
            }catch (NumberFormatException e){

            }
        }
        today = today.plusDays(selected.getDayOfWeek() - today.getDayOfWeek());
        monday = today.minusDays(today.getDayOfWeek() - 1);

        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.addObject("today", today);
        mav.addObject("isToday",isToday);
        mav.addObject("monday", monday);
        mav.addObject("todayAppointments", appointmentService.findToday(userStaffs));

        List<Appointment> appointments;
        if(monday.isAfter(DateTime.now())){
            appointments = appointmentService.findByStaffsAndDay(userStaffs, DateTime.now(), monday.plusDays(7));
        } else {
            appointments = appointmentService.findByStaffsAndDay(userStaffs, monday, monday.plusDays(7));
        }
        List<List<Appointment>> weekAppointments = new LinkedList<>();
        for(int i=0; i<=7; i++){
            weekAppointments.add(new LinkedList<>());
        }

        for (Appointment appointment : appointments){
            if(appointment.getFromDate().getDayOfWeek() < 1 || appointment.getFromDate().getDayOfWeek() > 7) {
                weekAppointments.get(0).add(appointment);
            } else {
                weekAppointments.get(appointment.getFromDate().getDayOfWeek()).add(appointment);
            }
        }
        mav.addObject("weekAppointments", weekAppointments); // lista de turnos que se muestra en la agenda semanal
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());
        mav.setViewName("medicSide/homeMedico");
        return mav;
    }

    @RequestMapping(value = "/staff/profile", method = RequestMethod.GET)
    public ModelAndView medicProfile(@ModelAttribute("medicProfileForm") final UserProfileForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
            mav.addObject("workdays", workdayService.findByUser(user.get()));
        }
        mav.addObject("verified", user.get().getVerified());
        mav.setViewName("medicSide/medicProfile");
        return mav;
    }

    @RequestMapping(value="/staff/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("medicProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }

        if (errors.hasErrors() || (!form.getPassword().isEmpty() && form.getPassword().length()<8) || !form.getPassword().equals(form.getRepeatPassword())) {
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
        editedUser.setPhone(form.getPhone());
        if(!form.getPassword().isEmpty())
            editedUser.setPassword(form.getPassword());
        userService.update(editedUser);
        if (!editedUser.getEmail().equals(user.get().getEmail())) {
            this.createConfirmationEvent(request, editedUser);
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.setViewName("medicSide/medicProfile");
        return mav;
    }

    @RequestMapping(value = "/staff/profile/confirm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse reverify(HttpServletRequest request, HttpServletResponse response) {
        return this.formatJsonResponse(() -> {
            Optional<User> user = getUser();
            if(!user.isPresent()) {
                throw new UnAuthorizedAccessException();
            }

            if (!user.get().getVerified()) {
                // Prevents jammering
                if (user.get().getTokenCreatedDate() == null || DateTime.now().isAfter(user.get().getTokenCreatedDate().plusMinutes(1)))
                    this.createConfirmationEvent(request, user.get());
                return true;
            }
            return false;
        });
    }

    @RequestMapping(value="/staff/profile/workday", method = RequestMethod.GET)
    public ModelAndView addWorkday(@ModelAttribute("workdayForm") final WorkdayForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.setViewName("medicSide/addTurno");
        return mav;
    }

    @RequestMapping(value="/staff/profile/workday", method = RequestMethod.POST)
    public ModelAndView addWorkdayAction(@Valid @ModelAttribute("workdayForm") final WorkdayForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }

        if (errors.hasErrors()) {
            return this.addWorkday(form);
        }


        Optional<Office> office = officeService.findById(form.getOfficeId());
        if(!office.isPresent()){
            return this.addWorkday(form);
        }

        Optional<Staff> realStaff = staffService.findByUser(user.get().getId()).stream().filter(staff -> staff.getOffice().equals(office.get())).findAny();
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

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.setViewName("redirect:/staff/profile");
        return mav;
    }

    @RequestMapping(value="/staff/profile/workday/delete/{workdayId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteWorkday(@PathVariable("workdayId") final int workdayId){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED); //TODO:check status code
        }
        Optional<Workday> workday = workdayService.findById(workdayId);
        if(!workday.isPresent() || !workday.get().getStaff().getUser().equals(user.get())){
            return new ResponseEntity(HttpStatus.FORBIDDEN);//TODO: check status code
        }

        workdayService.remove(workdayId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/staff/appointment/{id}",method = RequestMethod.DELETE)
    public ResponseEntity cancelAppointment(@PathVariable Integer id, HttpServletRequest request){
        //get current user, check for null
        Optional<User> user = getUser();
        if(!user.isPresent()){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //get staff for current user
        List<Staff> staff = this.staffService.findByUser(user.get().getId());
        //get appointment to delete, check for "null"
        Optional<Appointment> appointment = this.appointmentService.findById(id);
        if(!appointment.isPresent()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        //check if user is allowed to cancel
        boolean isAllowed = false;
        for(Staff s : staff){
            if(s.equals(appointment.get().getStaff())){
                isAllowed = true;
                break;
            }
        }
        //return response code for not allow
        if(!isAllowed){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //cancel appointment
        this.appointmentService.remove(appointment.get()); // TODO
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(new AppointmentCancelEvent(user.get(), true, appointment.get().getPatient().getUser(), appointment.get(), request.getLocale(), baseUrl.toString()));
//        this.appointmentService.setStatus(appointment.get(), AppointmentStatus.CANCELLED);
        //return success
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private void createConfirmationEvent(HttpServletRequest request, User user) {
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getRequestURI()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(
                new UserConfirmationTokenGenerationEvent(
                        baseUrl.toString(),
                        user,
                        request.getContextPath() + "/verifyEmail" ,
                        request.getLocale()
                )
        );
    }
}
