package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.webapp.exceptions.UnprocessableEntityException;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.WorkdayMIME;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import ar.edu.itba.paw.webapp.rest.utils.GenericResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;
import java.util.Optional;

@Path("/workdays")
@Component
public class WorkdayResource extends GenericResource {
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private DoctorService doctorService;

    @GET
    @Produces({WorkdayMIME.GET_LIST, ErrorMIME.ERROR})
    @PreAuthorize("hasRole('DOCTOR')")
    public Response getCollection(
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, WorkdayMIME.GET_LIST);

        User user = this.assertUserUnauthorized();
        if (!this.isDoctor()) throw this.forbidden();

        Optional<Doctor> doctorOptional = this.doctorService.findByUser(user).stream().findFirst();
        // TODO: Log, esto es inconsistencia, no deberia de pasar
        if (!doctorOptional.isPresent()) throw this.forbidden();

        return Response
                .ok()
                .entity(this.workdayService.findByDoctor(doctorOptional.get()))
                .type(WorkdayMIME.GET_LIST)
                .build();
    }

    @POST
    @Produces({WorkdayMIME.GET_LIST, ErrorMIME.ERROR})
    @Consumes(WorkdayMIME.CREATE_LIST)
    @PreAuthorize("hasRole('DOCTOR')")
    public Response createEntities(
            Collection<Workday> workdays,
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, WorkdayMIME.GET_LIST);

        if (workdays == null || workdays.isEmpty()) throw this.missingBodyParams();

        User user = this.assertUserUnauthorized();
        if (!this.isDoctor()) throw this.forbidden();

        Optional<Doctor> doctorOptional = this.doctorService.findByUser(user).stream().findFirst();
        // TODO: Log, esto es inconsistencia, no deberia de pasar
        if (!doctorOptional.isPresent()) throw this.forbidden();

        for (Workday workday : workdays) {
            if (workday.getStartHour() > workday.getEndHour()
                    || ((workday.getStartHour().equals(workday.getEndHour())) && (workday.getStartHour() > workday.getEndHour()))) {

                throw UnprocessableEntityException
                        .build()
                        .withReason(ErrorConstants.DATE_FROM_IS_AFTER_TO)
                        .getError();
            }
        }

        Collection<Workday> newWorkdays;
        try {
            newWorkdays = this.workdayService.create(workdays);
        } catch (Exception ignored) {
            // TODO: LOG
            throw ignored;
        }

        return Response
                .status(Status.CREATED)
                .entity(newWorkdays)
                .type(WorkdayMIME.GET_LIST)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({WorkdayMIME.GET, ErrorMIME.ERROR})
    @PreAuthorize("hasRole('DOCTOR')")
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, WorkdayMIME.GET);

        if (id == null) throw this.missingPathParams();

        User user = this.assertUserUnauthorized();
        if (!this.isDoctor()) throw this.forbidden();

        Optional<Doctor> doctorOptional = this.doctorService.findByUser(user).stream().findFirst();
        // TODO: Log, esto es inconsistencia, no deberia de pasar
        if (!doctorOptional.isPresent()) throw this.forbidden();

        Optional<Workday> workdayOptional = this.workdayService.findById(id);
        if (!workdayOptional.isPresent()) throw this.notFound();

        if (!doctorOptional.get().equals(workdayOptional.get().getDoctor()))
            throw this.notFound();

        return Response
                .ok()
                .entity(workdayOptional.get())
                .type(WorkdayMIME.GET)
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.WILDCARD, ErrorMIME.ERROR})
    @PreAuthorize("hasRole('DOCTOR')")
    public Response deleteEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        if (id == null) throw this.missingPathParams();

        User user = this.assertUserUnauthorized();
        if (!this.isDoctor()) throw this.forbidden();

        Optional<Doctor> doctorOptional = this.doctorService.findByUser(user).stream().findFirst();
        // TODO: Log, esto es inconsistencia, no deberia de pasar
        if (!doctorOptional.isPresent()) throw this.forbidden();

        Optional<Workday> workdayOptional = this.workdayService.findById(id);
        if (!workdayOptional.isPresent()) throw this.notFound();

        if (!workdayOptional.get().getDoctor().equals(doctorOptional.get()))
            throw this.notFound();

        this.workdayService.remove(id);

        return Response
                .status(Status.NO_CONTENT)
                .build();
    }
}
