package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.DoctorSpecialtyService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Paginator;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorSpecialty;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.DoctorMIME;
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
    public Response getCollection(
            @Context HttpHeaders httpheaders,
            @Context UriInfo uriInfo,
            @QueryParam("name") String name,
            @QueryParam("specialties") String specialties,
            @QueryParam("localities") String localities,
            @QueryParam(PAGINATOR_PAGE_QUERY) Integer page,
            @QueryParam(PAGINATOR_PER_PAGE_QUERY) Integer perPage) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET_LIST);

        // TODO: <= o < ??
        if (page != null) {
            if (page <= 0)
                return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());
        } else {
            page = 1;
        }
        if (perPage != null) {
            if (perPage <= 0)
                return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());
        } else {
            perPage = PAGINATOR_PER_PAGE_DEFAULT;
        }

        Collection<DoctorSpecialty> searchedSpecialties = this.doctorSpecialtyService.findByIds(this.stringToIntegerList(specialties));
        Collection<Locality> searchedLocalities = this.localityService.findByIds(this.stringToIntegerList(localities));

        Paginator<Doctor> doctorPaginator;
        if (name != null && !(name = name.trim()).equals("")) {
            Set<String> words = new HashSet<>(Arrays.asList(name.split(" ")));
            doctorPaginator = this.doctorService.findBy(words, words, null, searchedSpecialties, searchedLocalities, page, perPage);
        } else {
            doctorPaginator = this.doctorService.findBy((String) null, null, null, searchedSpecialties, searchedLocalities, page, perPage);
        }

        return this.createPaginatorResponse(doctorPaginator, uriInfo).build();
    }

    @GET
    @Path("{id}")
    @Produces({DoctorMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, DoctorMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Doctor> doctorOptional = this.doctorService.findById(id);
        if (!doctorOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response.ok(doctorOptional.get()).build();
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

        if (id == null || doctor == null || doctor.getDoctorSpecialties().isEmpty())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Doctor> doctorOptional = this.doctorService.findById(id);
        if (!doctorOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        Collection<DoctorSpecialty> doctorSpecialties = this.doctorSpecialtyService.findByIds(
                doctor.getDoctorSpecialties()
                        .stream()
                        .map(DoctorSpecialty::getId)
                        .collect(Collectors.toList())
        ); // TODO: Is this necessary?
        if (doctorSpecialties.size() != doctor.getDoctorSpecialties().size())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Doctor savedDoctor = doctorOptional.get();
        savedDoctor.setEmail(doctor.getEmail());
        savedDoctor.setPhone(doctor.getPhone());
        savedDoctor.setDoctorSpecialties(doctorSpecialties);

        this.doctorService.update(savedDoctor);

        return Response.status(Status.CREATED).entity(doctorOptional.get()).build();
    }
}
