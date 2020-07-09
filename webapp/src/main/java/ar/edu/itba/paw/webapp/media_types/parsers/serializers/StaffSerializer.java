package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import org.json.JSONArray;
import org.json.JSONObject;

public class StaffSerializer extends JsonSerializer<Staff> {
    public static final StaffSerializer instance = new StaffSerializer();

    private StaffSerializer() {}

    @Override
    public Object toJson(Staff staff) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", staff.getId());
        jsonObject.put("phone", staff.getPhone());
        jsonObject.put("email", staff.getEmail());
        jsonObject.put("registrationNumber", staff.getRegistrationNumber());
        jsonObject.put("user", UserSerializer.instance.toJson(staff.getUser()));
        jsonObject.put("office", OfficeSerializer.instance.toJson(staff.getOffice()));

        JSONArray specialtiesArray = new JSONArray();
        for (StaffSpecialty staffSpecialty : staff.getStaffSpecialties()) {
            specialtiesArray.put(staffSpecialty.getId());
        }

        jsonObject.put("staffSpecialtyIds", specialtiesArray);

        return jsonObject;
    }
}
