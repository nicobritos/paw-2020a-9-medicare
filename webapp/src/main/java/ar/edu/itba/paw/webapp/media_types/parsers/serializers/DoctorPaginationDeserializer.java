package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.webapp.models.DoctorPagination;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DoctorPaginationDeserializer extends JsonDeserializer<DoctorPagination> {
    public static final DoctorPaginationDeserializer instance = new DoctorPaginationDeserializer();

    private DoctorPaginationDeserializer() {}

    @Override
    public DoctorPagination fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        DoctorPagination doctorPagination = new DoctorPagination();

        String name = this.getStringNull(jsonObject, "name");
        doctorPagination.setName(name == null ? null : name.trim());
        doctorPagination.setPage(this.getIntegerNonNull(jsonObject, "page", page -> page > 1));

        JsonNode node = jsonObject.get("perPage");
        if (node != null && !node.isNull()) {
            if (!node.isInt() || node.asInt() < 10 || node.asInt() > 100) {
                throw new IllegalArgumentException();
            }
            doctorPagination.setPerPage(node.asInt());
        }

        doctorPagination.setSpecialtyIds(this.getArrayAsInt(jsonObject, "specialties"));
        doctorPagination.setLocalityIds(this.getArrayAsInt(jsonObject, "localities"));

        return doctorPagination;
    }
}
