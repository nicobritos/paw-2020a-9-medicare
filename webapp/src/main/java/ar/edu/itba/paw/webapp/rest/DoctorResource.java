package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.DoctorSpecialtyService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorSpecialty;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Paginator;
import ar.edu.itba.paw.webapp.media_types.DoctorMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import ar.edu.itba.paw.webapp.rest.utils.GenericResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Response getCollection(
            @Context HttpHeaders httpheaders,
            @Context UriInfo uriInfo,
            @QueryParam("page") Integer page,
            @QueryParam("per_page") Integer perPage,
            @QueryParam("localities") List<Integer> localities,
            @QueryParam("specialties") List<Integer> specialties,
            @QueryParam("name") String name) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET_LIST);

        if (page == null) {
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_PAGINATION_MISSING_PAGE);
        } else if (page < 1) {
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_PAGINATION_INVALID_PAGE);
        }

        if (perPage == null) {
            perPage = DoctorResource.DEFAULT_PER_PAGE;
        } else if (perPage < DoctorResource.MIN_PER_PAGE || perPage > DoctorResource.MAX_PER_PAGE) {
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_PAGINATION_INVALID_PER_PAGE);
        }

        Collection<Locality> searchedLocalities;
        if (localities == null || localities.isEmpty()) {
            searchedLocalities = Collections.emptyList();
        } else if (localities.stream().anyMatch(Objects::isNull)) {
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_PAGINATION_INVALID_LOCALITIES);
        } else {
            searchedLocalities = this.localityService.findByIds(localities);
        }

        Collection<DoctorSpecialty> searchedSpecialties;
        if (specialties == null || specialties.isEmpty()) {
            searchedSpecialties = Collections.emptyList();
        } else if (specialties.stream().anyMatch(Objects::isNull)) {
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_PAGINATION_INVALID_SPECIALTIES);
        } else {
            searchedSpecialties = this.doctorSpecialtyService.findByIds(specialties);
        }

        if (name != null) name = name.trim();

        Paginator<Doctor> paginationResponse;
        if (name != null && !name.isEmpty()) {
            Set<String> words = new HashSet<>(Arrays.asList(name.split(" ")));
            paginationResponse = this.doctorService.findBy(
                    words,
                    words,
                    null,
                    searchedSpecialties,
                    searchedLocalities,
                    page,
                    perPage
            );
        } else {
            paginationResponse = this.doctorService.findBy(
                    (String) null,
                    null,
                    null,
                    searchedSpecialties,
                    searchedLocalities,
                    page,
                    perPage
            );
        }

        return this.createPaginatorResponse(paginationResponse, uriInfo)
                .type(DoctorMIME.GET_LIST)
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
    @PreAuthorize("hasRole('DOCTOR')")
    public Response updateEntity(
            Doctor doctor,
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET);

        if (id == null) throw this.missingPathParams();
        if (doctor == null) throw this.missingBodyParams();
        if (doctor.getDoctorSpecialties().isEmpty()) {
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_UPDATE_EMPTY_SPECIALTIES);
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
            throw this.unprocessableEntity(ErrorConstants.DOCTOR_UPDATE_SOME_INVALID_SPECIALTIES);
        }

        Doctor savedDoctor = doctorOptional.get();
        savedDoctor.setEmail(doctor.getEmail());
        savedDoctor.setPhone(doctor.getPhone());
        savedDoctor.setDoctorSpecialties(doctorSpecialties);

        this.doctorService.update(savedDoctor);

        return Response.status(Status.CREATED).entity(doctorOptional.get()).type(DoctorMIME.GET).build();
    }
}
