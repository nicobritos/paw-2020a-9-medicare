package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.ConflictException;
import ar.edu.itba.paw.webapp.exceptions.UnprocessableEntityException;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.UserMIME;
import ar.edu.itba.paw.webapp.models.*;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import ar.edu.itba.paw.webapp.rest.utils.GenericAuthenticationResource;
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
import java.util.Collections;
import java.util.Optional;

@Path("/users")
@Component
public class UserResource extends GenericAuthenticationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
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
        MIMEHelper.assertServerType(httpheaders, UserMIME.ME);

        if (this.getUser().isPresent()) throw this.forbidden();

        if (this.userService.findByUsername(doctorSignUp.getUser().getEmail()).isPresent())
            throw ConflictException.build().withReason(ErrorConstants.USER_EMAIL_USED).getError();

        Optional<Locality> locality = this.localityService.findById(doctorSignUp.getLocalityId());
        if (!locality.isPresent()) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.USER_CREATE_NONEXISTENT_LOCALITY)
                    .getError();
        }

        User user = this.copyUser(doctorSignUp);

        Office office = new Office();
        office.setLocality(locality.get());
        office.setStreet(doctorSignUp.getStreet());
        // TODO: Guido i18n
        office.setName("Consultorio de " + user.getFirstName() + " " + user.getSurname());
        office.setPhone(doctorSignUp.getUser().getPhone());
        office.setPhone(doctorSignUp.getUser().getEmail());

        Doctor doctor = new Doctor();
        doctor.setOffice(office);
        doctor.setDoctorSpecialties(Collections.emptyList());
        doctor.setRegistrationNumber(doctorSignUp.getRegistrationNumber());
        doctor.setPhone(office.getPhone());
        doctor.setEmail(office.getEmail());

        User newUser = this.userService.createAsDoctor(user, doctor);
        this.finishSignUp(request, response, doctorSignUp, newUser);
        return Response
                .created(this.joinPaths(this.baseUrl, this.apiPath, "/users/" + newUser.getId()))
                .entity(UserMeFactory.withDoctors(newUser, Collections.singletonList(doctor)))
                .type(UserMIME.ME)
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
        MIMEHelper.assertServerType(httpheaders, UserMIME.ME);
        if (this.getUser().isPresent()) throw this.forbidden();
        if (this.userService.findByUsername(patientSignUp.getUser().getEmail()).isPresent())
            throw ConflictException.build().withReason(ErrorConstants.USER_EMAIL_USED).getError();

        User newUser = this.userService.create(this.copyUser(patientSignUp));
        this.finishSignUp(request, response, patientSignUp, newUser);
        return Response
                .created(this.joinPaths(this.baseUrl, this.apiPath, "/users/" + newUser.getId()))
                .entity(UserMeFactory.withPatients(newUser, Collections.emptyList()))
                .type(UserMIME.ME)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.GET);

        if (id == null) throw this.missingPathParams();

        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent()) throw this.notFound();

        return Response.ok(userOptional.get()).type(UserMIME.GET).build();
    }

    @GET
    @Produces({UserMIME.ME, ErrorMIME.ERROR})
    public Response getLoggedUser(
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.ME);

        User user = this.assertUserUnauthorized();

        UserMe userMe;
        if (this.isDoctor()) {
            userMe = UserMeFactory.withDoctors(user, this.doctorService.findByUser(user));
        } else {
            userMe = UserMeFactory.withPatients(user, this.patientService.findByUser(user));
        }

        return Response
                .ok()
                .entity(userMe)
                .type(UserMIME.ME)
                .build();
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

        if (id == null || user == null) throw this.missingBodyParams();
        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent()) throw this.notFound();

        User savedUser = userOptional.get();
        if (user.getEmail() != null)
            savedUser.setEmail(user.getEmail());
        if (user.getPhone() != null)
            savedUser.setPhone(user.getPhone());
        if (user.getSurname() != null)
            savedUser.setSurname(user.getSurname());
        if (user.getFirstName() != null)
            savedUser.setFirstName(user.getFirstName());
        // TODO
//        savedUser.setProfilePicture(user.getEmail());

        this.userService.update(savedUser);

        return Response.status(Status.CREATED).entity(savedUser).type(UserMIME.GET).build();
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
        this.userService.generateVerificationToken(newUser, request.getLocale(), "/verify");

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(userSignUp.getUser().getEmail());
        credentials.setPassword(userSignUp.getUser().getPassword());

        this.createJWTCookies(credentials, newUser, response, LOGGER);
    }
}
