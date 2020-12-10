package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class UserCreateDeserializer<T> extends JsonDeserializer<T> {
    protected User getUser(ObjectNode jsonObject) {
        User user = new User();

        user.setEmail(this.getStringNonNull(jsonObject, "email", this.emailValidator));
        user.setFirstName(this.getStringNonNull(jsonObject, "firstName", s -> s.length() >= 2 && s.length() <= 20));
        user.setSurname(this.getStringNonNull(jsonObject, "surname", s -> s.length() >= 2 && s.length() <= 20));
        user.setPassword(this.getStringNonNull(jsonObject, "password", s -> s.length() >= 8 && s.length() <= 100));

        JsonNode node = jsonObject.get("phone");
        if (node != null && !node.isNull()) {
            user.setPhone(node.asText());
        } else {
            user.setPhone(null);
        }

        return user;
    }
}
