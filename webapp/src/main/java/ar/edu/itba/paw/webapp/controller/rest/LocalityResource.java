package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.LocalityMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("/localities")
@Component
public class LocalityResource extends GenericResource {
    @Autowired
    private LocalityService localityService;

    @GET
    @Produces({LocalityMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, LocalityMIME.GET_LIST);

        return Response
                .ok()
                .entity(this.localityService.list())
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({LocalityMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, LocalityMIME.GET);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Locality> localityOptional = this.localityService.findById(id);
        if (!localityOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response.ok(localityOptional.get()).build();
    }
}
