package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.DoctorSpecialtyService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorSpecialty;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Paginator;
import ar.edu.itba.paw.webapp.exceptions.UnprocessableEntityException;
import ar.edu.itba.paw.webapp.media_types.DoctorMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.models.DoctorPagination;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import ar.edu.itba.paw.webapp.rest.utils.GenericResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.*;
import java.util.stream.Collectors;

@Path("/doctors")
@Component
public class DoctorResource extends GenericResource {
    @Autowired
    private DoctorSpecialtyService doctorSpecialtyService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private LocalityService localityService;

    // TODO: i18n
    @GET
    @Produces({DoctorMIME.GET_LIST, ErrorMIME.ERROR})
    @Consumes({DoctorMIME.PAGINATION})
    public Response getCollection(
            @Context HttpHeaders httpheaders,
            @Context UriInfo uriInfo,
            DoctorPagination pagination) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET_LIST);

        Collection<DoctorSpecialty> searchedSpecialties;
        if (pagination.getSpecialtyIds() != null && pagination.getSpecialtyIds().size() > 0)
            searchedSpecialties = this.doctorSpecialtyService.findByIds(pagination.getSpecialtyIds());
        else
            searchedSpecialties = Collections.emptyList();

        Collection<Locality> searchedLocalities;
        if (pagination.getLocalityIds() != null && pagination.getLocalityIds().size() > 0)
            searchedLocalities = this.localityService.findByIds(pagination.getLocalityIds());
        else
            searchedLocalities = Collections.emptyList();

        Paginator<Doctor> paginationResponse;
        if (pagination.getName() != null && pagination.getName().isEmpty()) {
            Set<String> words = new HashSet<>(Arrays.asList(pagination.getName().split(" ")));
            paginationResponse = this.doctorService.findBy(
                    words,
                    words,
                    null,
                    searchedSpecialties,
                    searchedLocalities,
                    pagination.getPage(),
                    pagination.getPerPage()
            );
        } else {
            paginationResponse = this.doctorService.findBy(
                    (String) null,
                    null,
                    null,
                    searchedSpecialties,
                    searchedLocalities,
                    pagination.getPage(),
                    pagination.getPerPage()
            );
        }

        return this.createPaginatorResponse(paginationResponse, uriInfo)
                .type(DoctorMIME.PAGINATION)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({DoctorMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET);

        if (id == null) throw this.missingPathParams();

        Optional<Doctor> doctorOptional = this.doctorService.findById(id);
        if (!doctorOptional.isPresent()) throw this.notFound();

        return Response.ok(doctorOptional.get()).type(DoctorMIME.GET).build();
    }

    @PUT
    @Path("{id}")
    @Produces({DoctorMIME.GET, ErrorMIME.ERROR})
    @Consumes(DoctorMIME.UPDATE)
    public Response updateEntity(
            Doctor doctor,
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET);

        if (id == null) throw this.missingPathParams();
        if (doctor == null) throw this.missingBodyParams();
        if (doctor.getDoctorSpecialties().isEmpty()) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.DOCTOR_UPDATE_EMPTY_SPECIALTIES)
                    .getError();
        }

        Optional<Doctor> doctorOptional = this.doctorService.findById(id);
        if (!doctorOptional.isPresent()) throw this.notFound();

        Collection<DoctorSpecialty> doctorSpecialties = this.doctorSpecialtyService.findByIds(
                doctor.getDoctorSpecialties()
                        .stream()
                        .map(DoctorSpecialty::getId)
                        .collect(Collectors.toList())
        );
        if (doctorSpecialties.size() != doctor.getDoctorSpecialties().size()) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.DOCTOR_UPDATE_SOME_INVALID_SPECIALTIES)
                    .getError();
        }

        Doctor savedDoctor = doctorOptional.get();
        savedDoctor.setEmail(doctor.getEmail());
        savedDoctor.setPhone(doctor.getPhone());
        savedDoctor.setDoctorSpecialties(doctorSpecialties);

        this.doctorService.update(savedDoctor);

        return Response.status(Status.CREATED).entity(doctorOptional.get()).type(DoctorMIME.GET).build();
    }
}
