package ar.edu.itba.paw.webapp.media_types.parsers;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.webapp.media_types.AppointmentMIME;
import ar.edu.itba.paw.webapp.media_types.parsers.serializers.AppointmentDeserializer;
import ar.edu.itba.paw.webapp.media_types.parsers.utils.GenericParser;
import ar.edu.itba.paw.webapp.media_types.parsers.utils.ParserUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Consumes(AppointmentMIME.CREATE)
public class AppointmentCreateParser extends GenericParser<Appointment> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Appointment readFrom(Class<Appointment> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try {
            return AppointmentDeserializer.instance.fromJson(ParserUtils.inputToJSON(inputStream));
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }
}
