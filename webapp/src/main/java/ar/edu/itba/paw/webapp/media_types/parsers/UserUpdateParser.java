package ar.edu.itba.paw.webapp.media_types.parsers;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.media_types.UserMIME;
import ar.edu.itba.paw.webapp.media_types.parsers.serializers.UserDeserializer;
import ar.edu.itba.paw.webapp.media_types.parsers.utils.GenericParser;
import ar.edu.itba.paw.webapp.media_types.parsers.utils.ParserUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(UserMIME.UPDATE)
public class UserUpdateParser extends GenericParser<User> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public User readFrom(Class<User> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try {
            return UserDeserializer.instance.fromJson(ParserUtils.inputToJSON(inputStream));
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }
}