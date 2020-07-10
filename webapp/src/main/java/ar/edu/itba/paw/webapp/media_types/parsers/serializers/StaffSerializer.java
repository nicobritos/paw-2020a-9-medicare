package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StaffSerializer extends JsonSerializer<Staff> {
    public static final StaffSerializer instance = new StaffSerializer();

    private StaffSerializer() {}

    @Override
    public JsonNode toJson(Staff staff) {
        ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();

        jsonObject.put("id", staff.getId());
        jsonObject.put("phone", staff.getPhone());
        jsonObject.put("email", staff.getEmail());
        jsonObject.put("registrationNumber", staff.getRegistrationNumber());

        // El metodo .put(String, JsonNode) esta deprecado, y cambiar la estructura de los
        // serializer no tiene sentido
        jsonObject.replace("user", UserSerializer.instance.toJson(staff.getUser()));
        jsonObject.replace("office", OfficeSerializer.instance.toJson(staff.getOffice()));

        ArrayNode specialtiesArray = JsonNodeFactory.instance.arrayNode();
        for (StaffSpecialty staffSpecialty : staff.getStaffSpecialties()) {
            specialtiesArray.add(staffSpecialty.getId());
        }

        jsonObject.replace("staffSpecialtyIds", specialtiesArray);

        return jsonObject;
    }
}
