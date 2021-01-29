package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.CountryService;
import ar.edu.itba.paw.interfaces.services.ProvinceService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.MIMEHelper;
import ar.edu.itba.paw.webapp.media_types.ProvinceMIME;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import ar.edu.itba.paw.webapp.rest.utils.GenericResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;

@Path("/countries/{countryId}/provinces")
@Component
public class ProvinceResource extends GenericResource {
    @Autowired
    private CountryService countryService;
    @Autowired
    private ProvinceService provinceService;

    @GET
    @Produces({ProvinceMIME.GET_LIST, ErrorMIME.ERROR})
    public Response getCollection(
            @Context HttpHeaders httpheaders,
            @PathParam("countryId") String countryId) {
        MIMEHelper.assertServerType(httpheaders, ProvinceMIME.GET_LIST);

        Collection<Province> provinces;
        if (countryId != null) {
            Optional<Country> country = this.countryService.findById(countryId);
            if (!country.isPresent()) {
                throw this.unprocessableEntity(ErrorConstants.PROVINCE_GET_NONEXISTENT_COUNTRY);
            }
            provinces = this.provinceService.findByCountry(country.get());
        } else {
            provinces = this.provinceService.list();
        }

        return Response
                .ok()
                .entity(provinces)
                .type(ProvinceMIME.GET_LIST)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({ProvinceMIME.GET, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpHeaders httpheaders,
            @PathParam("id") Integer id) {
        MIMEHelper.assertServerType(httpheaders, ProvinceMIME.GET_LIST);

        if (id == null) throw this.missingPathParams();

        Optional<Province> provinceOptional = this.provinceService.findById(id);
        if (!provinceOptional.isPresent()) throw this.notFound();

        return Response.ok(provinceOptional.get()).type(ProvinceMIME.GET).build();
    }
}
