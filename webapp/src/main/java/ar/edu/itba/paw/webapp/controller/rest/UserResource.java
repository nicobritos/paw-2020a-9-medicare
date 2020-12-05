package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericAuthenticationResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.UserMIME;
import ar.edu.itba.paw.webapp.models.PatientSignUp;
import ar.edu.itba.paw.webapp.models.StaffSignUp;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import ar.edu.itba.paw.webapp.models.UserSignUp;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
@Component
public class UserResource extends GenericAuthenticationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private StaffSpecialtyService staffSpecialtyService;
    @Autowired
    private LocalityService localityService;
    @Autowired
    private UserService userService;

    @POST
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
    @Consumes(UserMIME.CREATE_STAFF)
    public Response createStaff(
            StaffSignUp staffSignUp,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, UserMIME.GET);
        if (this.getUser().isPresent())
            return this.error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN.toString());
        if (this.userService.findByUsername(staffSignUp.getUser().getEmail()).isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Locality> locality = this.localityService.findById(staffSignUp.getOffice().getLocality().getId());
        if (!locality.isPresent())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Collection<StaffSpecialty> staffSpecialties = this.staffSpecialtyService.findByIds(
                staffSignUp
                        .getStaffSpecialties()
                        .stream()
                        .map(StaffSpecialty::getId)
                        .collect(Collectors.toList())
        );
        if (staffSpecialties.size() != staffSignUp.getStaffSpecialties().size())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        User user = this.copyUser(staffSignUp);

        Office office = new Office();
        office.setLocality(locality.get());
        office.setStreet(staffSignUp.getOffice().getStreet());

        if (staffSignUp.getOffice().getName() != null) {
            office.setName(staffSignUp.getOffice().getName());
        } else {
            // TODO: i18n
            office.setName("Consultorio de " + user.getFirstName() + " " + user.getSurname());
        }
        if (staffSignUp.getOffice().getPhone() != null) {
            office.setName(staffSignUp.getOffice().getPhone());
        } else {
            office.setPhone(staffSignUp.getUser().getPhone());
        }
        if (staffSignUp.getOffice().getEmail() != null) {
            office.setName(staffSignUp.getOffice().getEmail());
        } else {
            office.setPhone(staffSignUp.getUser().getEmail());
        }

        Staff staff = new Staff();
        staff.setOffice(office);
        staff.setStaffSpecialties(staffSpecialties);
        staff.setRegistrationNumber(staffSignUp.getRegistrationNumber());
        staff.setPhone(office.getPhone());
        staff.setEmail(office.getEmail());

        return this.finishSignUp(request, response, staffSignUp, this.userService.createAsStaff(user, staff));
    }

    @POST
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
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

        return this.finishSignUp(request, response, patientSignUp, this.userService.create(this.copyUser(patientSignUp)));
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

    private Response finishSignUp(HttpServletRequest request, HttpServletResponse response, UserSignUp userSignUp, User newUser) {
        this.sendConfirmationEmail(request, newUser);

        ResponseBuilder responseBuilder = Response.status(Status.CREATED);

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(userSignUp.getUser().getEmail());
        credentials.setPassword(userSignUp.getUser().getPassword());

        this.createJWTHeaders(responseBuilder, credentials, newUser, response, LOGGER);
        return responseBuilder
                .entity(newUser)
                .build();
    }
}
