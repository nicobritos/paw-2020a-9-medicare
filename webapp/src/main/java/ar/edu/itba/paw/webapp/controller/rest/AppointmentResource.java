package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericResource;
import ar.edu.itba.paw.webapp.media_types.AppointmentMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Path("/appointments")
@Component
public class AppointmentResource extends GenericResource {
    private static final long MAX_DAYS_APPOINTMENTS = 62;

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PatientService patientService;

    @GET
    @Produces({AppointmentMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @QueryParam("from_year") Integer fromYear,
            @QueryParam("from_month") Integer fromMonth,
            @QueryParam("from_day") Integer fromDay,
            @QueryParam("to_year") Integer toYear,
            @QueryParam("to_month") Integer toMonth,
            @QueryParam("to_day") Integer toDay) {
        MIMEHelper.assertAcceptedTypes(httpheaders, AppointmentMIME.GET_LIST);

        User user = this.getUser().get();
        Collection<Staff> staffs;
        Collection<Patient> patients;
        if (this.isStaff()) {
            patients = Collections.emptyList();
            staffs = this.staffService.findByUser(user);
            if (staffs.isEmpty())
                return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        } else {
            staffs = Collections.emptyList();
            patients = this.patientService.findByUser(user);
            if (patients.isEmpty())
                return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        }

        if (fromYear == null || fromMonth == null || fromDay == null || toYear == null || toMonth == null || toDay == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        LocalDateTime dateFrom = new LocalDateTime(fromYear, fromMonth, fromDay, 0, 0);
        LocalDateTime dateTo = new LocalDateTime(toYear, toMonth, toDay, 23, 59, 59, 999);

        long daysBetween = Days.daysBetween(dateFrom.toLocalDate(), dateTo.toLocalDate()).getDays();
        if (daysBetween > MAX_DAYS_APPOINTMENTS || dateTo.isBefore(dateFrom))
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        if (this.isStaff()) {
            return Response.ok(this.appointmentService.findByStaffsAndDay(staffs, dateFrom, dateTo)).build();
        } else {
            return Response.ok(this.appointmentService.findByPatientsAndDay(patients, dateFrom, dateTo)).build();
        }
    }

    @POST
    @Produces({AppointmentMIME.GET, ErrorMIME.ERROR})
    @Consumes(AppointmentMIME.CREATE)
    public Response createEntity(
            Appointment appointment,
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertAcceptedTypes(httpheaders, AppointmentMIME.GET);

        if (appointment == null || appointment.getFromDate() == null || appointment.getFromDate().isBefore(LocalDateTime.now()))
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());
        if (this.isStaff())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Staff> staffOptional = this.staffService.findById(appointment.getStaff().getId());
        if (!staffOptional.isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        User user = this.getUser().get();
        Optional<Patient> patientOptional = this.patientService.findByUserAndOffice(user, staffOptional.get().getOffice());
        if (!patientOptional.isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Appointment newAppointment = new Appointment();
        newAppointment.setStaff(staffOptional.get());
        newAppointment.setMotive(appointment.getMotive());
        newAppointment.setMessage(appointment.getMessage());
        newAppointment.setPatient(patientOptional.get());
        newAppointment.setFromDate(appointment.getFromDate());

        return Response
                .status(Status.CREATED)
                .entity(this.appointmentService.create(newAppointment))
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({AppointmentMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertAcceptedTypes(httpheaders, AppointmentMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        User user = this.getUser().get();
        Collection<Staff> staffs;
        Collection<Patient> patients;
        if (this.isStaff()) {
            patients = Collections.emptyList();
            staffs = this.staffService.findByUser(user);
            if (staffs.isEmpty())
                return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        } else {
            staffs = Collections.emptyList();
            patients = this.patientService.findByUser(user);
            if (patients.isEmpty())
                return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        }

        Optional<Appointment> appointmentOptional = this.appointmentService.findById(id);
        if (!appointmentOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        if (this.isStaff()) {
            if (!staffs.contains(appointmentOptional.get().getStaff()))
                return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        } else {
            if (!patients.contains(appointmentOptional.get().getPatient()))
                return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        }

        return Response.ok(appointmentOptional.get()).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.WILDCARD, ErrorMIME.ERROR})
    public Response deleteEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        if (this.isStaff())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Appointment> appointmentOptional = this.appointmentService.findById(id);
        if (!appointmentOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        User user = this.getUser().get();
        Collection<Patient> patients = this.patientService.findByUser(user);
        if (patients.isEmpty())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        if (!patients.contains(appointmentOptional.get().getPatient()))
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        this.appointmentService.remove(appointmentOptional.get().getId());

        return Response.status(Status.NO_CONTENT).build();
    }
}
