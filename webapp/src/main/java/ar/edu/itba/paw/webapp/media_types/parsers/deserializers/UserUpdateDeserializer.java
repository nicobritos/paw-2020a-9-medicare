package ar.edu.itba.paw.webapp.media_types.parsers.deserializers;

import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserUpdateDeserializer extends JsonDeserializer<User> {
    public static final UserUpdateDeserializer instance = new UserUpdateDeserializer();

    private UserUpdateDeserializer() {}

    @Override
    public User fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        User user = new User();

        JsonNode node = jsonObject.get("email");
        if (node != null && !node.isNull()) {
            user.setEmail(node.asText());
        }
        node = jsonObject.get("firstName");
        if (node != null && !node.isNull()) {
            user.setFirstName(node.asText());
        }
        node = jsonObject.get("surname");
        if (node != null && !node.isNull()) {
            user.setSurname(node.asText());
        }
        node = jsonObject.get("phone");
        if (node != null && !node.isNull()) {
            user.setPhone(node.asText());
        }

        node = jsonObject.get("profilePictureId");
        if (node != null && !node.isNull()) {
            Picture picture = new Picture();
            picture.setId(node.asInt());
            user.setProfilePicture(picture);
        }

        return user;
    }
}
