package ar.edu.itba.paw.webapp.controller.patient;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.interfaces.services.exceptions.InvalidAppointmentDateException;
import ar.edu.itba.paw.interfaces.services.exceptions.InvalidMinutesException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.controller.utils.JsonResponse;
import ar.edu.itba.paw.webapp.events.AppointmentCancelEvent;
import ar.edu.itba.paw.webapp.events.NewAppointmentEvent;
import ar.edu.itba.paw.webapp.events.UserConfirmationTokenGenerationEvent;
import ar.edu.itba.paw.webapp.exceptions.UnAuthorizedAccessException;
import ar.edu.itba.paw.webapp.form.RequestAppointmentForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
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
import java.util.List;
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
    @Autowired
    private PatientService patientService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @RequestMapping("/patient/home")
    public ModelAndView patientHome(){
        Optional<User> user = this.getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }
        ModelAndView mav = new ModelAndView();

        List<Patient> patients = patientService.findByUser(user.get());

        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.addObject("appointments", appointmentService.findByPatientsFromDate(patients, DateTime.now()));
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());

        mav.setViewName("patientSide/homePaciente");
        return mav;
    }

    @RequestMapping(value = "/patient/profile", method = RequestMethod.GET)
    public ModelAndView patientProfile(@ModelAttribute("patientProfileForm") final UserProfileForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        } else {
            mav.addObject("patients", patientService.findByUser(user.get()));
        }

        mav.addObject("verified", user.get().getVerified());
        mav.setViewName("patientSide/patientProfile");
        return mav;
    }

    @RequestMapping(value="/patient/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("patientProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("redirect:login");
        }

        if (errors.hasErrors()) {
            return this.patientProfile(form);
        }
        if((!form.getPassword().isEmpty() && form.getPassword().length()<8) || !form.getPassword().equals(form.getRepeatPassword())){
            errors.reject("Min.patientProfileForm.password", null, "Error");
            return this.patientProfile(form);
        }
        Optional<User> userOptional = userService.findByUsername(form.getEmail());
        if(userOptional.isPresent() && !userOptional.get().equals(user.get())){ // si se edito el email pero ya existe cuenta con ese email
            errors.reject("AlreadyExists.patientProfileForm.email", null, "Error");
            return this.patientProfile(form);
        }

        User editedUser = user.get();
        editedUser.setFirstName(form.getFirstName());
        editedUser.setSurname(form.getSurname());
        editedUser.setEmail(form.getEmail());
        editedUser.setPhone(form.getPhone());
        if(!form.getPassword().isEmpty())
            editedUser.setPassword(form.getPassword());
        userService.update(editedUser);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.setViewName("patientSide/patientProfile");
        return mav;
    }

    @RequestMapping(value = "/patient/appointment/{staffId}/{year}/{month}/{day}/{hour}/{minute}", method = RequestMethod.POST)
    public ModelAndView requestAppointment(@Valid @ModelAttribute("appointmentForm") RequestAppointmentForm form, final BindingResult errors,
                                           @PathVariable("staffId") final int staffId, @PathVariable("year") final int year,
                                           @PathVariable("month") final int month, @PathVariable("day") final int day,
                                           @PathVariable("hour") final int hour, @PathVariable("minute") final int minute, HttpServletRequest request){
        if(errors.hasErrors()){
            return requestAppointment(form, staffId, year, month, day, hour, minute);
        }
        Optional<Staff> staff = this.staffService.findById(form.getStaffId());
        if (!staff.isPresent()) {
            errors.reject("NotFound.requestAppointment.staff", null, "Error");
            return this.requestAppointment(form, staffId, year, month, day, hour, minute);
        }
        DateTime dateFrom = new DateTime(form.getYear(), form.getMonth(), form.getDay(), form.getHour(), form.getMinute());
        if (dateFrom.isBefore(DateTime.now())) {
            errors.reject("PastRequest.requestAppointment.date", null, "Error");
            return this.requestAppointment(form, staffId, year, month, day, hour, minute);
        }
        Optional<User> optionalUser = getUser();
        if(!optionalUser.isPresent()) {
            errors.reject("NotNull.requestAppointment.user", null, "Error");
            return this.requestAppointment(form, staffId, year, month, day, hour, minute);
        }
        Optional<Patient> patientOptional = this.patientService.findByUserAndOffice(optionalUser.get(), staff.get().getOffice());
        Patient patient;
        if (!patientOptional.isPresent()) {
            patient = new Patient();
            patient.setOffice(staff.get().getOffice());
            patient.setUser(optionalUser.get());
            patient = this.userService.createNewPatient(patient);
        } else {
            patient = patientOptional.get();
        }

        Appointment appointment = new Appointment();
        appointment.setStaff(staff.get());
        appointment.setPatient(patient);
        appointment.setFromDate(dateFrom);
        try {
            appointment = this.appointmentService.create(appointment);
            StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
            baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
            this.eventPublisher.publishEvent(new NewAppointmentEvent(optionalUser.get(), staff.get().getUser(), appointment, request.getLocale(), baseUrl.toString(), form.getMotive(), form.getComment()));
        } catch (InvalidMinutesException e){
            errors.reject("InvalidValue.requestAppointment.date", null, "Error");
            return this.requestAppointment(form, staffId, year, month, day, hour, minute);
        } catch (InvalidAppointmentDateException e){
            errors.reject("MedicNotWorking.requestAppointment.date", null, "Error");
            return this.requestAppointment(form, staffId, year, month, day, hour, minute);
        }
        return new ModelAndView("redirect:/patient/home");
    }

    @RequestMapping("/patient/appointment/{staffId}/{year}/{month}/{day}/{hour}/{minute}")
    public ModelAndView requestAppointment(@ModelAttribute("appointmentForm") RequestAppointmentForm form,
                                        @PathVariable("staffId") final int staffId, @PathVariable("year") final int year,
                                           @PathVariable("month") final int month, @PathVariable("day") final int day,
                                        @PathVariable("hour") final int hour, @PathVariable("minute") final int minute) {
        Optional<User> userOptional = getUser();
        form.setDay(day);
        form.setMonth(month);
        form.setYear(year);
        form.setHour(hour);
        form.setMinute(minute);
        form.setStaffId(staffId);
        Optional<Staff> staffOptional = staffService.findById(staffId);
        if (!staffOptional.isPresent()) {
            return new ModelAndView("redirect:/mediclist/0");
        }
        Optional<StaffSpecialty> staffSpecialty = staffOptional.get().getStaffSpecialties().stream().findFirst();
        if(staffSpecialty.isPresent()){
            form.setMotive("Consulta de " + staffSpecialty.get().getName());
        } else {
            form.setMotive("Consulta");
        }
        userOptional.ifPresent(user -> {
            form.setEmail(user.getEmail());
            form.setFirstName(user.getFirstName());
            form.setSurname(user.getSurname());
            form.setPhone(user.getPhone());
        });
        Optional<User> user = getUser();
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", userOptional);
        if (user.isPresent() && isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        staffOptional.ifPresent(staff -> mav.addObject("staff", staff));
        mav.addObject("date", new DateTime(year, month, day, hour, minute));
        mav.setViewName("patientSide/reservarTurno");
        return mav;
    }

    @RequestMapping(value = "/patient/appointment/{id}",method = RequestMethod.DELETE)
    public ResponseEntity cancelAppointment(@PathVariable Integer id, HttpServletRequest request){
        //get current user, check for null
        Optional<User> user = getUser();
        if(!user.isPresent()){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //get patient for current user
        List<Patient> patient = this.patientService.findByUser(user.get());
        //get appointment to delete, check for "null"
        Optional<Appointment> appointment = this.appointmentService.findById(id);
        if(!appointment.isPresent()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        //check if user is allowed to cancel
        boolean isAllowed = false;
        for(Patient p : patient){
            if(p.equals(appointment.get().getPatient())){
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
        this.eventPublisher.publishEvent(new AppointmentCancelEvent(user.get(), false, appointment.get().getStaff().getUser(), appointment.get(), request.getLocale(), baseUrl.toString()));
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
                        request.getContextPath() + "/verifyEmail",
                        request.getLocale()
                )
        );
    }
}