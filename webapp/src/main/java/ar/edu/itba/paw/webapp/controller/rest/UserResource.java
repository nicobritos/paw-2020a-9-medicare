package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.DoctorSpecialtyService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericAuthenticationResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.UserMIME;
import ar.edu.itba.paw.webapp.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
@Component
public class UserResource extends GenericAuthenticationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private DoctorSpecialtyService doctorSpecialtyService;
    @Autowired
    private LocalityService localityService;
    @Autowired
    private UserService userService;
    @Autowired
    private String baseUrl;
    @Autowired
    private String apiPath;

    @POST
    @Produces({UserMIME.ME, ErrorMIME.ERROR})
    @Consumes(UserMIME.CREATE_DOCTOR)
    public Response createDoctor(
            DoctorSignUp doctorSignUp,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.GET);
        if (this.getUser().isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        if (this.userService.findByUsername(doctorSignUp.getUser().getEmail()).isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Locality> locality = this.localityService.findById(doctorSignUp.getOffice().getLocality().getId());
        if (!locality.isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Collection<DoctorSpecialty> doctorSpecialties = this.doctorSpecialtyService.findByIds(
                doctorSignUp
                        .getDoctorSpecialties()
                        .stream()
                        .map(DoctorSpecialty::getId)
                        .collect(Collectors.toList())
        );
        if (doctorSpecialties.size() != doctorSignUp.getDoctorSpecialties().size())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        User user = this.copyUser(doctorSignUp);

        Office office = new Office();
        office.setLocality(locality.get());
        office.setStreet(doctorSignUp.getOffice().getStreet());

        if (doctorSignUp.getOffice().getName() != null) {
            office.setName(doctorSignUp.getOffice().getName());
        } else {
            // TODO: i18n
            office.setName("Consultorio de " + user.getFirstName() + " " + user.getSurname());
        }
        if (doctorSignUp.getOffice().getPhone() != null) {
            office.setName(doctorSignUp.getOffice().getPhone());
        } else {
            office.setPhone(doctorSignUp.getUser().getPhone());
        }
        if (doctorSignUp.getOffice().getEmail() != null) {
            office.setName(doctorSignUp.getOffice().getEmail());
        } else {
            office.setPhone(doctorSignUp.getUser().getEmail());
        }

        Doctor doctor = new Doctor();
        doctor.setOffice(office);
        doctor.setDoctorSpecialties(doctorSpecialties);
        doctor.setRegistrationNumber(doctorSignUp.getRegistrationNumber());
        doctor.setPhone(office.getPhone());
        doctor.setEmail(office.getEmail());

        User newUser = this.userService.createAsDoctor(user, doctor);
        this.finishSignUp(request, response, doctorSignUp, newUser);
        return Response
                .created(this.joinPaths(this.baseUrl, this.apiPath, "/users/" + newUser.getId()))
                .entity(UserMeFactory.withDoctors(newUser, Collections.singletonList(doctor)))
                .build();
    }

    @POST
    @Produces({UserMIME.ME, ErrorMIME.ERROR})
    @Consumes(UserMIME.CREATE_PATIENT)
    public Response createPatient(
            PatientSignUp patientSignUp,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.GET);
        if (this.getUser().isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        if (this.userService.findByUsername(patientSignUp.getUser().getEmail()).isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        User newUser = this.userService.create(this.copyUser(patientSignUp));
        this.finishSignUp(request, response, patientSignUp, newUser);
        return Response
                .created(this.joinPaths(this.baseUrl, this.apiPath, "/users/" + newUser.getId()))
                .entity(UserMeFactory.withPatients(newUser, Collections.emptyList()))
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response.ok(userOptional.get()).build();
    }

    @PUT
    @Path("{id}")
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
    @Consumes(UserMIME.UPDATE)
    public Response updateEntity(
            User user,
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.GET);

        if (id == null || user == null || user.getFirstName() == null || user.getSurname() == null || user.getEmail() == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        User savedUser = userOptional.get();
        savedUser.setEmail(user.getEmail());
        savedUser.setPhone(user.getPhone());
        savedUser.setSurname(user.getSurname());
        savedUser.setFirstName(user.getFirstName());
        // TODO
//        savedUser.setProfilePicture(user.getEmail());

        this.userService.update(savedUser);

        return Response.status(Status.CREATED).entity(savedUser).build();
    }

    private User copyUser(UserSignUp userSignUp) {
        User user = new User();

        user.setFirstName(userSignUp.getUser().getFirstName());
        user.setSurname(userSignUp.getUser().getSurname());
        user.setPhone(userSignUp.getUser().getPhone());
        user.setEmail(userSignUp.getUser().getEmail());
        user.setPassword(userSignUp.getUser().getPassword());

        return user;
    }

    private void finishSignUp(HttpServletRequest request, HttpServletResponse response, UserSignUp userSignUp, User newUser) {
        userService.generateVerificationToken(newUser, request.getLocale(), "/verify");

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(userSignUp.getUser().getEmail());
        credentials.setPassword(userSignUp.getUser().getPassword());

        this.createJWTCookies(credentials, newUser, response, LOGGER);
    }
}
