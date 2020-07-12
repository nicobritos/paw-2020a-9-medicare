package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.StaffSpecialtyMIME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("/specialties")
@Component
public class StaffSpecialtyResource extends GenericResource {
    @Autowired
    private StaffSpecialtyService staffSpecialtyService;

    @GET
    @Produces({StaffSpecialtyMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders) {
        MIMEHelper.assertServerType(httpheaders, StaffSpecialtyMIME.GET_LIST);

        return Response
                .ok()
                .entity(this.staffSpecialtyService.list())
                .build();
    }
}
