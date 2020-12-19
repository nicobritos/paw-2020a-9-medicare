package ar.edu.itba.paw.webapp.media_types.parsers.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.io.InputStream;

public abstract class ParserUtils {
    // Devuelve un ObjectNode o ArrayNode
    public static JsonNode inputToJSON(InputStream inputStream) throws IOException, BadRequestException {
        // Tratamos de parsearlo
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(inputStream);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error parsing JSON body");
        }
    }
}
