package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.webapp.media_types.AppointmentMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
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
import java.util.LinkedList;
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
            @QueryParam("to_day") Integer toDay)
    {
        this.assertAcceptedTypes(httpheaders, AppointmentMIME.GET_LIST);

        // TODO: Get staff/patient id from JWT
        Integer staffId = 1;
        Integer patientId = 1;
        if (staffId == null && patientId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        if (fromYear == null || fromMonth == null || fromDay == null || toYear == null || toMonth == null || toDay == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        LocalDateTime dateFrom = new LocalDateTime(fromYear, fromMonth, fromDay, 0, 0);
        LocalDateTime dateTo = new LocalDateTime(toYear, toMonth, toDay, 23, 59, 59, 999);

        long daysBetween = Days.daysBetween(dateFrom.toLocalDate(), dateTo.toLocalDate()).getDays();
        if (daysBetween > MAX_DAYS_APPOINTMENTS || dateTo.isBefore(dateFrom))
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        if (staffId != null) {
            Optional<Staff> staffOptional = this.staffService.findById(staffId);
            if (!staffOptional.isPresent())
                return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

            Collection<Staff> staffWrapper = new LinkedList<>();
            staffWrapper.add(staffOptional.get());

            return Response.ok(this.appointmentService.findByStaffsAndDay(staffWrapper, dateFrom, dateTo)).build();
        } else {
            Optional<Patient> patientOptional = this.patientService.findById(staffId);
            if (!patientOptional.isPresent())
                return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

            Collection<Patient> patientWrapper = new LinkedList<>();
            patientWrapper.add(patientOptional.get());

            return Response.ok(this.appointmentService.findByPatientsAndDay(patientWrapper, dateFrom, dateTo)).build();
        }
    }

    @POST
    @Produces({AppointmentMIME.GET, ErrorMIME.ERROR})
    @Consumes(AppointmentMIME.CREATE)
    public Response createEntity(
            Appointment appointment,
            @Context HttpHeaders httpheaders)
    {
        this.assertAcceptedTypes(httpheaders, AppointmentMIME.GET);

        if (appointment == null || appointment.getFromDate() == null || appointment.getFromDate().isBefore(LocalDateTime.now()))
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get patient id from JWT
        Integer patientId = 1;
        if (patientId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Patient> patientOptional = this.patientService.findById(patientId);
        if (!patientOptional.isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Staff> staffOptional = this.staffService.findById(appointment.getStaff().getId());
        if (!staffOptional.isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

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
    @Path("/{id}")
    @Produces({AppointmentMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        this.assertAcceptedTypes(httpheaders, AppointmentMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get patient id from JWT
        Integer patientId = 1;
        if (patientId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Appointment> appointmentOptional = this.appointmentService.findById(id);
        if (!appointmentOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());
        if (!appointmentOptional.get().getPatient().getId().equals(patientId))
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        return Response.ok(appointmentOptional.get()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.WILDCARD, ErrorMIME.ERROR})
    public Response deleteEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get patient id from JWT
        Integer patientId = 1;
        if (patientId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Appointment> appointmentOptional = this.appointmentService.findById(id);
        if (!appointmentOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());
        if (!appointmentOptional.get().getPatient().getId().equals(patientId))
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        this.appointmentService.remove(patientId);

        return Response.status(Status.NO_CONTENT).build();
    }
}
