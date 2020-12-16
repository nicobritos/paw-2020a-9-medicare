package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exceptions.UnprocessableEntityException;
import ar.edu.itba.paw.webapp.media_types.AppointmentTimeSlotMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import ar.edu.itba.paw.webapp.rest.utils.GenericResource;
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
import java.util.Optional;

@Path("/appointmentTimeSlots")
@Component
public class AppointmentTimeSlotResource extends GenericResource {
    private static final long MAX_DAYS_APPOINTMENTS = 7;

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorService doctorService;

    @GET
    @Produces({AppointmentTimeSlotMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders,
            @QueryParam("doctor_id") Integer doctorId,
            @QueryParam("from_year") Integer fromYear,
            @QueryParam("from_month") Integer fromMonth,
            @QueryParam("from_day") Integer fromDay,
            @QueryParam("to_year") Integer toYear,
            @QueryParam("to_month") Integer toMonth,
            @QueryParam("to_day") Integer toDay) {
        MIMEHelper.assertServerType(httpheaders, AppointmentTimeSlotMIME.GET_LIST);

        if (fromYear == null || fromMonth == null || fromDay == null || toYear == null || toMonth == null || toDay == null)
            throw this.missingQueryParams();

        Optional<Doctor> doctorOptional = this.doctorService.findById(doctorId);
        if (!doctorOptional.isPresent()) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.APPOINTMENT_TIME_SLOT_GET_NONEXISTENT_DOCTOR)
                    .getError();
        }

        LocalDateTime dateFrom, dateTo;
        try {
            dateFrom = new LocalDateTime(fromYear, fromMonth, fromDay, 0, 0);
            dateTo = new LocalDateTime(toYear, toMonth, toDay, 23, 59, 59, 999);
        } catch (Exception e) {
            throw this.invalidQueryParams();
        }

        long daysBetween = Days.daysBetween(dateFrom.toLocalDate(), dateTo.toLocalDate()).getDays();
        if (daysBetween > MAX_DAYS_APPOINTMENTS) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.DATE_RANGE_TOO_BROAD)
                    .getError();
        } else if (dateTo.isBefore(dateFrom)) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.DATE_FROM_IS_AFTER_TO)
                    .getError();
        }

        return Response
                .ok(this.appointmentService.findAvailableTimeslotsInDateInterval(doctorOptional.get(), dateFrom, dateTo))
                .type(AppointmentTimeSlotMIME.GET_LIST)
                .build();
    }
}
