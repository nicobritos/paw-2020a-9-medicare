package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.models.User;
import org.json.JSONObject;

public class UserDeserializer extends JsonDeserializer<User> {
    public static final UserDeserializer instance = new UserDeserializer();

    private UserDeserializer() {}

    @Override
    public User fromJson(Object o) {
        if (!(o instanceof JSONObject)) {
            throw new IllegalArgumentException();
        }

        JSONObject jsonObject = (JSONObject) o;

        User user = new User();

        user.setEmail(jsonObject.getString("email"));
        user.setFirstName(jsonObject.getString("firstName"));
        user.setSurname(jsonObject.getString("surname"));
        user.setPhone(jsonObject.getString("phone"));

        Picture picture = new Picture();
        picture.setId(jsonObject.getInt("profilePictureId"));

        user.setProfilePicture(picture);

        return user;
    }
}
