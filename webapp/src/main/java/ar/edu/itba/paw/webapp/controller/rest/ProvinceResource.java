package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.ProvinceService;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.ProvinceMIME;
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

@Path("/provinces")
@Component
public class ProvinceResource extends GenericResource {
    @Autowired
    private ProvinceService provinceService;

    @GET
    @Produces({ProvinceMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders)
    {
        this.assertAcceptedTypes(httpheaders, ProvinceMIME.GET_LIST);

        return Response
                .ok()
                .entity(this.provinceService.list())
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({ProvinceMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id)
    {
        this.assertAcceptedTypes(httpheaders, ProvinceMIME.GET_LIST);

        if (id == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<Province> provinceOptional = this.provinceService.findById(id);
        if (!provinceOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        return Response.ok(provinceOptional.get()).build();
    }
}
