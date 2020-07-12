package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.CountryService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericResource;
import ar.edu.itba.paw.webapp.media_types.CountryMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
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

@Path("/countries")
@Component
public class CountryResource extends GenericResource {
    @Autowired
    private CountryService countryService;

    @GET
    @Produces({CountryMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertAcceptedTypes(httpheaders, CountryMIME.GET_LIST);

        return Response
                .ok()
                .entity(this.countryService.list())
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({CountryMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") String id) {
        MIMEHelper.assertAcceptedTypes(httpheaders, CountryMIME.GET_LIST);

        if (id == null || id.isEmpty())
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Country> countryOptional = this.countryService.findById(id);
        if (!countryOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response.ok(countryOptional.get()).build();
    }
}
