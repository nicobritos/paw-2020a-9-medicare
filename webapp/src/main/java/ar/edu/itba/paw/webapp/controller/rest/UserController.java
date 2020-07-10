package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.UserMIME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("/users")
@Component
public class UserController extends GenericRestController {
    @Autowired
    private UserService userService;

    @GET
    @Path("/{id}")
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        this.assertAcceptedTypes(httpheaders, UserMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response.ok(userOptional.get()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({UserMIME.GET, ErrorMIME.ERROR})
    @Consumes(UserMIME.UPDATE)
    public Response updateEntity(
            User user,
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        this.assertAcceptedTypes(httpheaders, UserMIME.GET);

        if (id == null || user.getFirstName() == null || user.getSurname() == null || user.getEmail() == null)
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

        return Response.ok(savedUser).build();
    }
}
