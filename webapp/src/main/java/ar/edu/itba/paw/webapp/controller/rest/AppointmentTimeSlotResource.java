package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.webapp.media_types.AppointmentTimeSlotMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("/appointmentTimeSlots")
@Component
public class AppointmentTimeSlotResource extends GenericResource {
    private static final long MAX_DAYS_APPOINTMENTS = 7;

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private StaffService staffService;

    @GET
    @Produces({AppointmentTimeSlotMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders,
            @QueryParam("staff_id") Integer staffId,
            @QueryParam("from_year") Integer fromYear,
            @QueryParam("from_month") Integer fromMonth,
            @QueryParam("from_day") Integer fromDay,
            @QueryParam("to_year") Integer toYear,
            @QueryParam("to_month") Integer toMonth,
            @QueryParam("to_day") Integer toDay)
    {
        this.assertAcceptedTypes(httpheaders, AppointmentTimeSlotMIME.GET_LIST);

        if (staffId == null || fromYear == null || fromMonth == null || fromDay == null || toYear == null || toMonth == null || toDay == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Staff> staffOptional = this.staffService.findById(staffId);
        if (!staffOptional.isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        LocalDateTime dateFrom = new LocalDateTime(fromYear, fromMonth, fromDay, 0, 0);
        LocalDateTime dateTo = new LocalDateTime(toYear, toMonth, toDay, 23, 59, 59, 999);

        long daysBetween = Days.daysBetween(dateFrom.toLocalDate(), dateTo.toLocalDate()).getDays();
        if (daysBetween > MAX_DAYS_APPOINTMENTS || dateTo.isBefore(dateFrom))
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        return Response
                .ok(this.appointmentService.findAvailableTimeslots(staffOptional.get(), dateFrom, dateTo))
                .build();
    }
}
