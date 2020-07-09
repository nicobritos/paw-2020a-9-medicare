package ar.edu.itba.paw.webapp.media_types.parsers;

import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.webapp.media_types.StaffMIME;
import ar.edu.itba.paw.webapp.media_types.parsers.serializers.StaffSerializer;
import org.glassfish.jersey.message.internal.AbstractMessageReaderWriterProvider;
import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

@Produces(StaffMIME.GET)
public class StaffGetParser extends AbstractMessageReaderWriterProvider<Collection<Staff>> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    @Override
    public Collection<Staff> readFrom(Class<Collection<Staff>> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        throw new BadRequestException();
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Collection<Staff> staffs, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, ReaderWriter.getCharset(mediaType));
            writer.write(StaffSerializer.instance.toJsonArray(staffs).toString());
            writer.flush();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }
}
