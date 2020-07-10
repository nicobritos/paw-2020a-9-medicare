package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.models.Paginator;
import ar.edu.itba.paw.webapp.exceptions.MissingAcceptsException;
import ar.edu.itba.paw.webapp.media_types.ApplicationMIME;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.models.APIError;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class GenericRestController {
    protected static final String PAGINATOR_PAGE_QUERY = "page";
    protected static final String PAGINATOR_PER_PAGE_QUERY = "per_page";
    protected static final int PAGINATOR_PER_PAGE_DEFAULT = 10;

    protected Response error(int code, String message) {
        return Response
                .status(code)
                .entity(new APIError(code, message))
                .build();
    }

    protected void assertAcceptedTypes(HttpHeaders httpHeaders, String... types) {
        Set<String> mediaTypes = httpHeaders.getAcceptableMediaTypes()
                .stream()
                .map(MediaType::toString)
                .collect(Collectors.toSet());
        if (mediaTypes.contains(MediaType.WILDCARD) || mediaTypes.contains(ApplicationMIME.WILDCARD))
            return;

        // Como cada endpoint puede devolver un error, entonces lo comparamos contra ese tipo
        if (!mediaTypes.contains(ErrorMIME.ERROR))
            throw new MissingAcceptsException();

        for (String type : types) {
            if (!mediaTypes.contains(type))
                throw new MissingAcceptsException();
            mediaTypes.remove(type);
        }
    }

    protected ResponseBuilder createPaginatorResponse(Paginator<?> paginator, UriInfo uriInfo) {
        int nextPage = 0, previousPage = 0;
        int firstPage = 1, lastPage = paginator.getTotalPages();

        if (paginator.getRemainingPages() > 0) {
            nextPage = paginator.getPage() + 1;
        }
        if (paginator.getPage() > firstPage) {
            previousPage = paginator.getPage() - 1;
        }

        StringBuilder header = new StringBuilder();
        header
                .append("<")
                .append(this.formatPaginatorUrl(firstPage, paginator.getPageSize(), uriInfo))
                .append(">; rel=")
                .append("\"first\"")
                .append(", <")
                .append(this.formatPaginatorUrl(lastPage, paginator.getPageSize(), uriInfo))
                .append(">; rel=")
                .append("\"last\"");
        if (nextPage > 0) {
            header
                    .append(", <")
                    .append(this.formatPaginatorUrl(nextPage, paginator.getPageSize(), uriInfo))
                    .append(">; rel=")
                    .append("\"next\"");
        }
        if (previousPage > 0) {
            header
                    .append(", <")
                    .append(this.formatPaginatorUrl(previousPage, paginator.getPageSize(), uriInfo))
                    .append(">; rel=")
                    .append("\"previous\"");
        }

        return Response
                .ok()
                .entity(paginator.getModels())
                .header("Link", header.toString())
                .header("Total-Items", String.valueOf(paginator.getTotalCount()));
    }

    private String formatPaginatorUrl(int page, int perPage, UriInfo uriInfo) {
        try {
            return uriInfo.getAbsolutePath().toString()
                    + "?"
                    + URLEncoder.encode(PAGINATOR_PAGE_QUERY, StandardCharsets.UTF_8.name())
                    + "="
                    + page
                    + "&"
                    + URLEncoder.encode(PAGINATOR_PER_PAGE_QUERY, StandardCharsets.UTF_8.name())
                    + "="
                    + perPage;
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException();
        }
    }
}
