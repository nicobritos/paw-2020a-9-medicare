package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.User;
import org.json.JSONObject;

public class UserSerializer extends JsonSerializer<User> {
    public static final UserSerializer instance = new UserSerializer();

    private UserSerializer() {}

    @Override
    public Object toJson(User user) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", user.getId());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("surname", user.getSurname());
        jsonObject.put("verified", user.getVerified());
        jsonObject.put("phone", user.getPhone());
        jsonObject.put("profilePictureId", user.getProfilePicture().getId());

        return jsonObject;
    }
}
