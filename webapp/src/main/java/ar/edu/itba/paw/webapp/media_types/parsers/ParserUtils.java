package ar.edu.itba.paw.webapp.media_types.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.BadRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ParserUtils {
    public static String inputToString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    // Devuelve un JSONObject o JSONArray
    public static Object inputToJSON(InputStream inputStream) throws IOException, BadRequestException {
        Object o;
        // Guardamos el input
        String input = ParserUtils.inputToString(inputStream);
        // Tratamos de parsearlo
        try {
            o = new JSONObject(input);
        } catch (JSONException e) {
            try {
                o = new JSONArray(input);
            } catch (JSONException ignored) {
                throw new BadRequestException();
            }
        }

        return o;
    }
}
