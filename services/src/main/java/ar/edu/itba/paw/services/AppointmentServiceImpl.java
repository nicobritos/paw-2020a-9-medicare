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
    public List<Appointment> findByStaffs(List<Staff> staffs){
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
    public List<Appointment> findToday(List<Staff> staffs){
        return this.findByStaffsAndDay(staffs, DateTime.now());
    }

    @Override
    public List<Appointment> findToday(Patient patient){
        return this.repository.findByDate(patient, DateTime.now());
    }

    @Override
    public List<Appointment> findByDay(Staff staff, DateTime date){
        return this.repository.findByStaffsAndDate(Collections.singletonList(staff), date);
    }

    @Override
    public List<Appointment> findByStaffsAndDay(List<Staff> staffs, DateTime date){
        return this.repository.findByStaffsAndDate(staffs, date);
    }

    @Override
    public List<Appointment> findByStaffsAndDay(List<Staff> staffs, DateTime from, DateTime to){
        return this.repository.findByStaffsAndDate(staffs, from, to).stream().peek(this::completeAppointment).collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByPatientsAndDay(List<Patient> patients, DateTime date){
        return this.repository.findByPatientsAndDate(patients, date);
    }


    @Override
    public void setStatus(Appointment appointment, AppointmentStatus status) throws
            AppointmentAlreadyCancelledException,
            InvalidAppointmentStatusChangeException,
            AppointmentAlreadyCompletedException {
        if (appointment.getAppointmentStatus().equals(status.name()))
            return;

        if (appointment.getAppointmentStatus().equals(AppointmentStatus.CANCELLED.name())) {
            throw new AppointmentAlreadyCancelledException();
        } else if (appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETE.name())) {
            throw new AppointmentAlreadyCompletedException();
        } else if (!this.isValidStatusChange(appointment.getAppointmentStatus(), status.name())) {
            throw new InvalidAppointmentStatusChangeException(appointment.getAppointmentStatus(), status.name());
        }

        appointment.setAppointmentStatus(status.name());
        this.repository.update(appointment);
    }

    public Appointment create(Appointment model) throws InvalidAppointmentDateException, InvalidAppointmentDurationException {
        if (!this.isValidDate(model.getStaff(), model.getFromDate()))
            throw new InvalidAppointmentDateException();

        model.setAppointmentStatus(AppointmentStatus.PENDING.name());

        List<Appointment> appointments = this.findByDay(model.getStaff(), model.getFromDate());
        for (Appointment appointment : appointments){
            if (model.getFromDate().isAfter(appointment.getFromDate()) && model.getFromDate().isBefore(appointment.getToDate())
                    || (model.getToDate().isAfter(appointment.getFromDate()) && model.getToDate().isBefore(appointment.getToDate()))){
                throw new MediCareException("Workday date overlaps with an existing one");
            }
        }
        return this.repository.create(model);
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, DateTime fromDate, DateTime toDate) {
        DateTime now = DateTime.now();
        List<AppointmentTimeSlot> appointmentTimeSlots = new LinkedList<>();
        if (now.isBefore(fromDate)) {
            fromDate = now;
        }

        while (toDate.isAfter(fromDate)) {
            WorkdayDay workdayDay = WorkdayDay.from(fromDate);
            List<Workday> workdays = this.workdayService.findByStaff(staff, workdayDay); // Los horarios de ese día
            for (Workday workday : workdays) {
                // Primera hora
                for (int j = workday.getStartMinute(); j < 60; j += Appointment.DURATION) {
                    AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                    appointmentTimeSlot.setDate(new DateTime(
                            fromDate.getYear(),
                            fromDate.getMonthOfYear(),
                            fromDate.getDayOfMonth(),
                            workday.getStartHour(),
                            j,
                            fromDate.getZone()
                    ));
                    appointmentTimeSlots.add(appointmentTimeSlot);
                }
                // Resto de las horas menos la última
                for (int i = workday.getStartHour() + 1; i < workday.getEndHour(); i++) {
                    for (int j = 0; j < 60; j += Appointment.DURATION) {
                        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                        appointmentTimeSlot.setDate(new DateTime(
                                fromDate.getYear(),
                                fromDate.getMonthOfYear(),
                                fromDate.getDayOfMonth(),
                                i,
                                j,
                                fromDate.getZone()
                        ));
                        appointmentTimeSlots.add(appointmentTimeSlot);
                    }
                }
                //Ultima hora
                for (int j = 0; j < workday.getEndMinute(); j += Appointment.DURATION) {
                    AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                    appointmentTimeSlot.setDate(new DateTime(
                            fromDate.getYear(),
                            fromDate.getMonthOfYear(),
                            fromDate.getDayOfMonth(),
                            workday.getEndHour(),
                            j,
                            fromDate.getZone()
                    ));
                    appointmentTimeSlots.add(appointmentTimeSlot);
                }

                this.findByDay(staff, fromDate).forEach(appointment -> {
                            AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                            appointmentTimeSlot.setDate(new DateTime(
                                    appointment.getFromDate().getYear(),
                                    appointment.getFromDate().getMonthOfYear(),
                                    appointment.getFromDate().getDayOfMonth(),
                                    appointment.getFromDate().getHourOfDay(),
                                    appointment.getFromDate().getMinuteOfDay(),
                                    appointment.getFromDate().getZone()
                            ));
                            appointmentTimeSlots.remove(appointmentTimeSlot);
                        });
            }
            fromDate = fromDate.plusDays(1);
        }
        return appointmentTimeSlots;
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, DateTime date) {
        return this.findAvailableTimeslots(staff, date, date);
    }

    @Override
    protected AppointmentDao getRepository() {
        return this.repository;
    }

    private boolean isValidStatusChange(String appointmentStatus, String newStatus) {
        if (appointmentStatus.equals(AppointmentStatus.PENDING.name())) {
            return newStatus.equals(AppointmentStatus.WAITING.name()) || newStatus.equals(AppointmentStatus.CANCELLED.name());
        } else if (appointmentStatus.equals(AppointmentStatus.WAITING.name())) {
            return newStatus.equals(AppointmentStatus.SEEN.name()) || newStatus.equals(AppointmentStatus.CANCELLED.name());
        } else if (appointmentStatus.equals(AppointmentStatus.SEEN.name())) {
            return newStatus.equals(AppointmentStatus.COMPLETE.name());
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
