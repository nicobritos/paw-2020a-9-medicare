package ar.edu.itba.paw.webapp.media_types;

import ar.edu.itba.paw.webapp.exceptions.MissingAcceptsException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MIMEHelper {
    public static void assertAcceptedTypes(HttpServletRequest request, String... types) throws MissingAcceptsException {
        Enumeration headerNames = request.getHeaderNames();

        if (request.getHeaderNames() == null)
            throw new MissingAcceptsException();

        Set<String> mediaTypes = new HashSet<>();
        while (!headerNames.hasMoreElements()) {
            Object o = headerNames.nextElement();
            if (o instanceof String)
                mediaTypes.add(request.getHeader((String) o));
        }
        assertAcceptedTypes(mediaTypes, types);
    }

    public static void assertAcceptedTypes(HttpHeaders httpHeaders, String... types) throws MissingAcceptsException {
        Set<String> mediaTypes = httpHeaders.getAcceptableMediaTypes()
                .stream()
                .map(MediaType::toString)
                .collect(Collectors.toSet());

        assertAcceptedTypes(mediaTypes, types);
    }

    private static void assertAcceptedTypes(Collection<String> mediaTypes, String... types) throws MissingAcceptsException {
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
}
