package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<AppointmentDao, Appointment, Integer> implements AppointmentService {
    @Autowired
    private AppointmentDao repository;
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;

    @Override
    public List<Appointment> find(Staff staff) {
        return this.repository.find(staff);
    }

    @Override
    public List<Appointment> findByStaffs(List<Staff> staffs) {
        return this.repository.findByStaffs(staffs);
    }

    @Override
    public List<Appointment> find(Patient patient) {
        return this.repository.find(patient);
    }

    @Override
    public List<Appointment> findByPatients(List<Patient> patients) {
        return this.repository.findByPatients(patients);
    }

    @Override
    public List<Appointment> findByPatientsFromDate(List<Patient> patients, DateTime from) {
        return this.repository.findByPatientsFromDate(patients, from);
    }

    @Override
    public List<Appointment> findToday(List<Staff> staffs) {
        return this.findByStaffsAndDay(staffs, DateTime.now());
    }

    @Override
    public List<Appointment> findToday(Patient patient) {
        return this.repository.findByDate(patient, DateTime.now());
    }

    @Override
    public List<Appointment> findByDay(Staff staff, DateTime date) {
        return this.repository.findByStaffsAndDate(Collections.singletonList(staff), date);
    }

    @Override
    public List<Appointment> findByStaffsAndDay(List<Staff> staffs, DateTime date) {
        return this.repository.findByStaffsAndDate(staffs, date);
    }

    @Override
    public List<Appointment> findByStaffsAndDay(List<Staff> staffs, DateTime from, DateTime to) {
        return this.repository.findByStaffsAndDate(staffs, from, to);
    }

    @Override
    public List<Appointment> findByPatientsAndDay(List<Patient> patients, DateTime date) {
        return this.repository.findByPatientsAndDate(patients, date);
    }

    @Override
    public void setStatus(Appointment appointment, AppointmentStatus status) throws
            AppointmentAlreadyCancelledException,
            InvalidAppointmentStatusChangeException,
            AppointmentAlreadyCompletedException {
        if (appointment.getAppointmentStatus().equals(status))
            return;

        if (appointment.getAppointmentStatus().equals(AppointmentStatus.CANCELLED)) {
            throw new AppointmentAlreadyCancelledException();
        } else if (appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETE)) {
            throw new AppointmentAlreadyCompletedException();
        } else if (!this.isValidStatusChange(appointment.getAppointmentStatus(), status)) {
            throw new InvalidAppointmentStatusChangeException(appointment.getAppointmentStatus().name(), status.name());
        }

        appointment.setAppointmentStatus(status);
        this.repository.update(appointment);
    }

    public Appointment create(Appointment model) throws InvalidAppointmentDateException {
        if (model.getFromDate().getMinuteOfHour() % 15 != 0)
            throw new InvalidMinutesException();
        if (!this.isValidDate(model.getStaff(), model.getFromDate()))
            throw new InvalidAppointmentDateException();

        model.setAppointmentStatus(AppointmentStatus.PENDING);

        List<Appointment> appointments = this.findByDay(model.getStaff(), model.getFromDate());
        for (Appointment appointment : appointments) {
            if (model.getFromDate().isAfter(appointment.getFromDate()) && model.getFromDate().isBefore(appointment.getToDate())
                    || (model.getToDate().isAfter(appointment.getFromDate()) && model.getToDate().isBefore(appointment.getToDate()))) {
                throw new MediCareException("Workday date overlaps with an existing one");
            }
        }
        return this.repository.create(model);
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, DateTime fromDate, DateTime toDate) {
        DateTime now = DateTime.now();
        List<AppointmentTimeSlot> appointmentTimeSlots = new LinkedList<>();
        if (now.isAfter(fromDate)) {
            fromDate = now;
        }
        int hour = fromDate.getHourOfDay();
        int minute = fromDate.getMinuteOfHour();
        minute = (int) Math.ceil((double) minute / Appointment.DURATION) * Appointment.DURATION;
        if (minute == 60) {
            hour += 1;
            minute = 0;
            if (hour == 24) {
                fromDate.plusDays(1);
                hour = 0;
            }
        }
        fromDate = fromDate.withTime(hour, minute, 0, 0);
        while (toDate.isAfter(fromDate)) {
            WorkdayDay workdayDay = WorkdayDay.from(fromDate);
            List<Workday> workdays = this.workdayService.findByStaff(staff, workdayDay); // Los horarios de ese día
            for (Workday workday : workdays) {
                int startHour = workday.getStartHour();
                int firstStartMinute = workday.getStartMinute();
                if (fromDate.getHourOfDay() > workday.getStartHour() || (fromDate.getHourOfDay() == workday.getStartHour() && fromDate.getMinuteOfHour() > workday.getStartMinute())) {
                    startHour = fromDate.getHourOfDay();
                    firstStartMinute = fromDate.getMinuteOfHour();
                }
                for (int ihour = startHour; ihour <= workday.getEndHour(); ihour++) {

                    int startMinute = 0;
                    if (ihour == startHour) {
                        startMinute = firstStartMinute;
                    }
                    int endMinute = 60;
                    if (ihour == workday.getEndHour()) {
                        endMinute = workday.getEndMinute();
                    }
                    for (int imin = startMinute; imin < endMinute; imin += Appointment.DURATION) {
                        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                        appointmentTimeSlot.setDate(new DateTime(
                                fromDate.getYear(),
                                fromDate.getMonthOfYear(),
                                fromDate.getDayOfMonth(),
                                ihour,
                                imin,
                                fromDate.getZone()
                        ));
                        appointmentTimeSlots.add(appointmentTimeSlot);
                    }
                }

                this.findByDay(staff, fromDate).forEach(appointment -> {
                    AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                    appointmentTimeSlot.setDate(appointment.getFromDate());
                    appointmentTimeSlots.remove(appointmentTimeSlot);
                });
            }
            fromDate = fromDate.plusDays(1);
            fromDate = fromDate.withTime(0, 0, 0, 0);
        }
        return appointmentTimeSlots;
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, DateTime date) {
        return this.findAvailableTimeslots(staff, date, date.withTime(23, 59, 59, 999));
    }

    @Override
    protected AppointmentDao getRepository() {
        return this.repository;
    }

    private boolean isValidStatusChange(AppointmentStatus appointmentStatus, AppointmentStatus newStatus) {
        if (appointmentStatus.equals(AppointmentStatus.PENDING)) {
            return newStatus.equals(AppointmentStatus.WAITING) || newStatus.equals(AppointmentStatus.CANCELLED);
        } else if (appointmentStatus.equals(AppointmentStatus.WAITING)) {
            return newStatus.equals(AppointmentStatus.SEEN) || newStatus.equals(AppointmentStatus.CANCELLED);
        } else if (appointmentStatus.equals(AppointmentStatus.SEEN)) {
            return newStatus.equals(AppointmentStatus.COMPLETE);
        }
        return false;
    }

    private boolean isValidDate(Staff staff, DateTime fromDate) {
        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
        appointmentTimeSlot.setDate(fromDate);

        if (!this.workdayService.isStaffWorking(staff, appointmentTimeSlot))
            return false;
        return this.findAvailableTimeslots(staff, fromDate).contains(appointmentTimeSlot);
    }
}
