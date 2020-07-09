package ar.edu.itba.paw.webapp.media_types.parsers;

import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.webapp.media_types.StaffMIME;
import ar.edu.itba.paw.webapp.media_types.parsers.serializers.StaffDeserializer;
import org.glassfish.jersey.message.internal.AbstractMessageReaderWriterProvider;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Consumes(StaffMIME.UPDATE)
public class StaffUpdateParser extends AbstractMessageReaderWriterProvider<Staff> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Staff readFrom(Class<Staff> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try {
            return StaffDeserializer.instance.fromJson(ParserUtils.inputToJSON(inputStream));
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    @Override
    public void writeTo(Staff staff, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        throw new InternalServerErrorException();
    }
}
