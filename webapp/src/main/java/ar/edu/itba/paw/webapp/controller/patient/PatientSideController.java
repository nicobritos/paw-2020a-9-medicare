package ar.edu.itba.paw.webapp.controller.patient;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import ar.edu.itba.paw.webapp.controller.utils.JsonResponse;
import ar.edu.itba.paw.webapp.form.RequestAppointmentForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
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


    @RequestMapping("/patient/home")
    public ModelAndView patientHome(){
        Optional<User> user = this.getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }
        ModelAndView mav = new ModelAndView();

        List<Patient> patients = patientService.findByUser(user.get());

        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.addObject("appointments", appointmentService.findByPatients(patients));
        mav.addObject("specialties", staffSpecialtyService.list());
        mav.addObject("localities", localityService.list());

        mav.setViewName("patientSide/homePaciente");
        return mav;
    }

    @RequestMapping(value = "/patient/profile", method = RequestMethod.GET)
    public ModelAndView patientProfile(@ModelAttribute("patientProfileForm") final UserProfileForm form){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        } else {
            mav.addObject("patients", patientService.findByUser(user.get()));
        }

        mav.setViewName("patientSide/patientProfile");
        return mav;
    }

    @RequestMapping(value="/patient/profile", method = RequestMethod.POST)
    public ModelAndView editMedicUser(@Valid @ModelAttribute("patientProfileForm") final UserProfileForm form, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            return new ModelAndView("authentication/login");
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
        if(isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get().getId()));
        }
        mav.setViewName("patientSide/patientProfile");
        return mav;
    }

    @RequestMapping(value = "/patient/appointment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse makeAppointment(
            @Valid @RequestBody RequestAppointmentForm form,
            final BindingResult errors){
        return this.formatJsonResponse(errors, () -> {
            Optional<Staff> staff = this.staffService.findById(form.getStaffId());
            if (!staff.isPresent()) {
                throw new MediCareException("No existe el staff solicitado");
            }
            LocalDate dateFrom = LocalDate.of(form.getYear(), form.getMonth(), form.getDay());
            if (dateFrom.isBefore(LocalDate.now())) { // TODO: Check hour/minute
                throw new MediCareException("No puede sacar un turno en el pasado");
            }

            Optional<Patient> patientOptional = this.patientService.findByUserAndOffice(this.getUser().get(), staff.get().getOffice());
            Patient patient;
            if (!patientOptional.isPresent()) {
                patient = new Patient();
                patient.setOffice(staff.get().getOffice());
                patient.setUser(this.getUser().get());
                patient = this.userService.createNewPatient(patient);
            } else {
                patient = patientOptional.get();
            }

            LocalDateTime date = LocalDateTime.of(dateFrom.getYear(), dateFrom.getMonthValue(), dateFrom.getDayOfMonth(), form.getHour(), form.getMinute(), 0);
            Appointment appointment = new Appointment();
            appointment.setStaff(staff.get());
            appointment.setPatient(patient);
            // TODO
//            appointment.setFromDate(Date.valueOf(date.toLocalDate()));

            this.appointmentService.create(appointment);
            return new LinkedList<>();
        });
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
        staffOptional.get().getStaffSpecialties().stream().findFirst().ifPresent(specialty -> form.setMotive("Consulta de " + specialty.getName()));
        userOptional.ifPresent(user -> {
            form.setEmail(user.getEmail());
            form.setFirstName(user.getFirstName());
            form.setSurname(user.getSurname());
            form.setPhone(null); //todo: phone
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
    public ResponseEntity cancelAppointment(@PathVariable Integer id){
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
            if(p.getId().equals(appointment.get().getPatientId())){
                isAllowed = true;
                break;
            }
        }
        //return response code for not allow
        if(!isAllowed){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //cancel appointment
        this.appointmentService.setStatus(appointment.get(), AppointmentStatus.CANCELLED);
        //return success
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
