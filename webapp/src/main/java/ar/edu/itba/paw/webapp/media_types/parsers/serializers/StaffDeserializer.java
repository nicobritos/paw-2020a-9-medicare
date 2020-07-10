package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.LinkedList;

public class StaffDeserializer extends JsonDeserializer<Staff> {
    public static final StaffDeserializer instance = new StaffDeserializer();

    private StaffDeserializer() {}

    @Override
    public Staff fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        Staff staff = new Staff();

        JsonNode node = jsonObject.get("phone");
        if (!node.isNull()) {
            staff.setPhone(jsonObject.get("phone").asText());
        }
        node = jsonObject.get("email");
        if (!node.isNull()) {
            staff.setEmail(jsonObject.get("email").asText());
        }

        ArrayNode jsonSpecialtiesIds = (ArrayNode) jsonObject.get("staffSpecialtyIds");
        Collection<StaffSpecialty> staffSpecialties = new LinkedList<>();
        for (JsonNode o1 : jsonSpecialtiesIds) {
            if (!o1.isInt())
                throw new IllegalArgumentException();

            StaffSpecialty staffSpecialty = new StaffSpecialty();
            staffSpecialty.setId(o1.asInt());
            staffSpecialties.add(staffSpecialty);
        }

        staff.setStaffSpecialties(staffSpecialties);

        return staff;
    }
}
