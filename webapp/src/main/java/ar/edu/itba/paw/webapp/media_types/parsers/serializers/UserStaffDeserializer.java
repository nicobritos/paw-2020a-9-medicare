package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.models.StaffSignUp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.LinkedList;

public class UserStaffDeserializer extends UserCreateDeserializer<StaffSignUp> {
    public static final UserStaffDeserializer instance = new UserStaffDeserializer();

    private UserStaffDeserializer() {}

    @Override
    public StaffSignUp fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;
        JsonNode officeNode = jsonObject.get("office");
        if (!officeNode.isObject())
            throw new IllegalArgumentException();

        ObjectNode officeObject = (ObjectNode) officeNode;

        User user = this.getUser(jsonObject);

        StaffSignUp staffSignUp = new StaffSignUp();
        staffSignUp.setUser(user);
        staffSignUp.setRegistrationNumber(this.getIntegerNonNull(jsonObject, "registrationNumber"));

        Locality locality = new Locality();
        locality.setId(this.getIntegerNonNull(officeObject, "localityId"));

        Office office = new Office();
        office.setStreet(this.getStringNonNull(officeObject, "street", s -> s.length() >= 2));
        office.setPhone(this.getStringNull(officeObject, "phone"));
        office.setEmail(this.getStringNull(officeObject, "email"));
        office.setName(this.getStringNull(officeObject, "name"));

        ArrayNode jsonSpecialtiesIds = (ArrayNode) jsonObject.get("specialtyIds");
        Collection<StaffSpecialty> staffSpecialties = new LinkedList<>();
        for (JsonNode o1 : jsonSpecialtiesIds) {
            if (!o1.isInt())
                throw new IllegalArgumentException();

            StaffSpecialty staffSpecialty = new StaffSpecialty();
            staffSpecialty.setId(o1.asInt());
            staffSpecialties.add(staffSpecialty);
        }
        staffSignUp.setStaffSpecialties(staffSpecialties);

        return staffSignUp;
    }
}
