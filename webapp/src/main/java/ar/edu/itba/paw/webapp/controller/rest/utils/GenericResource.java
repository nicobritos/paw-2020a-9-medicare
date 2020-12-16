package ar.edu.itba.paw.webapp.controller.rest.utils;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Paginator;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.Constants;
import ar.edu.itba.paw.webapp.auth.UserRole;
import ar.edu.itba.paw.webapp.models.APIError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class GenericResource {
    protected static final int PAGINATOR_PER_PAGE_DEFAULT = 10;
    private static final String PAGINATOR_PAGE_QUERY = "page";
    private static final String PAGINATOR_PER_PAGE_QUERY = "per_page";

    @Autowired
    private UserService userService;

    protected boolean isDoctor() {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return false;

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.isEmpty())
            return false;

        GrantedAuthority authority = authorities.stream().findFirst().orElse(null);
        return authority.getAuthority().equals(UserRole.DOCTOR.getAsRole());
    }

    protected Optional<User> getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return this.userService.findByUsername(((org.springframework.security.core.userdetails.User) principal).getUsername());
        }
        return Optional.empty();
    }

    protected Response error(Status status) {
        return this.error(status.getStatusCode(), status.toString());
    }

    protected Response error(int code, String message) {
        return Response
                .status(code)
                .entity(new APIError(code, message))
                .build();
    }

    protected Set<Integer> stringToIntegerList(String list) {
        return this.stringToIntegerList(list, ",");
    }

    protected Set<Integer> stringToIntegerList(String list, String regexSeparator) {
        Set<Integer> specialtiesIds = new HashSet<>();
        if (list != null) {
            // split strings to get all items and create the list
            for (String s : list.split(regexSeparator)) {
                try {
                    int id = Integer.parseInt(s);
                    if (id >= 0) {
                        specialtiesIds.add(id);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return specialtiesIds;
    }

    protected ResponseBuilder createPaginatorResponse(Paginator<?> paginator, UriInfo uriInfo) {
        ResponseBuilder responseBuilder = Response
                .ok()
                .entity(paginator.getModels())
                .header(Constants.PAGINATOR_COUNT_HEADER, String.valueOf(paginator.getTotalCount()));

        if (paginator.getTotalCount() > 0) {
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

            responseBuilder.header(HttpHeaders.LINK, header.toString());
        }

        return responseBuilder;
    }

    protected URI joinPaths(String start, String... paths) {
        StringBuilder end = new StringBuilder(start);

        for (String path : paths) {
            String endString = end.toString();

            if (endString.endsWith("/") && path.startsWith("/")) {
                end.append(path, 1, path.length());
            } else if (endString.endsWith("/") || path.startsWith("/")) {
                end.append(path);
            } else {
                end.append("/").append(path);
            }
        }

        return URI.create(end.toString());
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
