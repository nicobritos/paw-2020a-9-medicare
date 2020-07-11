package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.WorkdayMIME;
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

@Path("/workdays")
@Component
public class WorkdayResource extends GenericResource {
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private StaffService staffService;

    @GET
    @Produces({WorkdayMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders)
    {
        this.assertAcceptedTypes(httpheaders, WorkdayMIME.GET_LIST);

        // TODO: Get staff id from JWT
        Integer staffId = 1;
        if (staffId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Staff> staffOptional = this.staffService.findById(staffId);
        if (!staffOptional.isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString()); // TODO: Log, esto es inconsistencia, no deberia de pasar

        return Response
                .ok()
                .entity(this.workdayService.findByStaff(staffOptional.get()))
                .build();
    }

    @POST
    @Produces({WorkdayMIME.GET_LIST, ErrorMIME.ERROR})
    @Consumes(WorkdayMIME.CREATE_LIST)
    public Response createEntities(
            Collection<Workday> workdays,
            @Context HttpHeaders httpheaders)
    {
        this.assertAcceptedTypes(httpheaders, WorkdayMIME.GET_LIST);

        if (workdays == null || workdays.isEmpty())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get staff id from JWT
        Integer staffId = 1;
        if (staffId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Staff> staffOptional = this.staffService.findById(staffId);
        if (!staffOptional.isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString()); // TODO: Log, esto es inconsistencia, no deberia de pasar

        Collection<Workday> newWorkdays = new LinkedList<>();

        Status error = null;
        for (Workday workday : workdays) {
            if (workday.getStartHour() > workday.getEndHour()
                || ((workday.getStartHour().equals(workday.getEndHour())) && (workday.getStartHour() > workday.getEndHour())))
            {
                // We need to rollback
                error = Status.BAD_REQUEST;
                break;
            }

            try {
                newWorkdays.add(this.workdayService.create(this.createWorkday(workday, staffOptional.get())));
            } catch (Exception e) {
                // We need to rollback
                error = Status.INTERNAL_SERVER_ERROR;
                break;
            }
        }

        // We need to rollback
        if (error != null) {
            for (Workday newWorkday : newWorkdays) {
                try {
                    this.workdayService.remove(newWorkday.getId());
                } catch (Exception ignored) {
                    // TODO: LOG
                }
            }
            return this.error(error.getStatusCode(), error.toString());
        }

        return Response
                .status(Status.CREATED)
                .entity(newWorkdays)
                .build();
    }

    @POST
    @Produces({WorkdayMIME.GET, ErrorMIME.ERROR})
    @Consumes(WorkdayMIME.CREATE)
    public Response createEntity(
            Workday workday,
            @Context HttpHeaders httpheaders)
    {
        this.assertAcceptedTypes(httpheaders, WorkdayMIME.GET);

        if (workday == null || workday.getDay() == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get staff id from JWT
        Integer staffId = 1;
        if (staffId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Staff> staffOptional = this.staffService.findById(staffId);
        if (!staffOptional.isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString()); // TODO: Log, esto es inconsistencia, no deberia de pasar

        if (
                workday.getStartHour() > workday.getEndHour()
                        || ((workday.getStartHour().equals(workday.getEndHour())) && (workday.getStartHour() > workday.getEndHour())))
        {
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());
        }

        return Response
                .status(Status.CREATED)
                .entity(this.workdayService.create(this.createWorkday(workday, staffOptional.get())))
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({WorkdayMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        this.assertAcceptedTypes(httpheaders, WorkdayMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get staff id from JWT
        Integer staffId = 1;
        if (staffId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Workday> workdayOptional = this.workdayService.findById(id);
        if (!workdayOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response
                .ok()
                .entity(workdayOptional.get())
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.WILDCARD, ErrorMIME.ERROR})
    public Response deleteEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        // TODO: Get staff id from JWT
        Integer staffId = 1;
        if (staffId == null)
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        Optional<Workday> workdayOptional = this.workdayService.findById(id);
        if (!workdayOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        if (!workdayOptional.get().getStaff().getId().equals(staffId))
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());

        this.workdayService.remove(id);

        return Response
                .status(Status.NO_CONTENT)
                .build();
    }

    private Workday createWorkday(Workday workday, Staff staff) {
        Workday copy = new Workday();

        copy.setStaff(staff);
        copy.setDay(workday.getDay());
        copy.setStartHour(workday.getStartHour());
        copy.setStartMinute(workday.getStartMinute());
        copy.setEndHour(workday.getEndHour());
        copy.setEndMinute(workday.getEndMinute());

        return copy;
    }
}
