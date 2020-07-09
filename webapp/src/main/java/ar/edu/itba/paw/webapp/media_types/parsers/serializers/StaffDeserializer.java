package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedList;

public class StaffDeserializer extends JsonDeserializer<Staff> {
    public static final StaffDeserializer instance = new StaffDeserializer();

    private StaffDeserializer() {}

    @Override
    public Staff fromJson(Object o) {
        if (!(o instanceof JSONObject)) {
            throw new IllegalArgumentException();
        }

        JSONObject jsonObject = (JSONObject) o;

        Staff staff = new Staff();

        staff.setPhone(jsonObject.getString("phone"));
        staff.setEmail(jsonObject.getString("email"));

        JSONArray jsonSpecialtiesIds = jsonObject.getJSONArray("staffSpecialtyIds");
        Collection<StaffSpecialty> staffSpecialties = new LinkedList<>();
        for (Object o1 : jsonSpecialtiesIds) {
            if (!(o1 instanceof Integer))
                throw new IllegalArgumentException();

            StaffSpecialty staffSpecialty = new StaffSpecialty();
            staffSpecialty.setId((Integer) o1);
            staffSpecialties.add(staffSpecialty);
        }

        staff.setStaffSpecialties(staffSpecialties);

        return staff;
    }
}
