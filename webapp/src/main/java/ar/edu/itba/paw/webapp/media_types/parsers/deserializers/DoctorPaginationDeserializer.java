package ar.edu.itba.paw.webapp.media_types.parsers.deserializers;

import ar.edu.itba.paw.webapp.exceptions.UnprocessableEntityException;
import ar.edu.itba.paw.webapp.models.DoctorPagination;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.BadRequestException;

public class DoctorPaginationDeserializer extends JsonDeserializer<DoctorPagination> {
    public static final DoctorPaginationDeserializer instance = new DoctorPaginationDeserializer();

    private DoctorPaginationDeserializer() {}

    @Override
    public DoctorPagination fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new BadRequestException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        DoctorPagination doctorPagination = new DoctorPagination();

        String name = this.getStringNull(jsonObject, "name", ErrorConstants.DOCTOR_PAGINATION_INVALID_NAME);
        doctorPagination.setName(name == null ? null : name.trim());
        doctorPagination.setPage(this.getIntegerNonNull(
                jsonObject,
                "page",
                page -> page >= 1,
                ErrorConstants.DOCTOR_PAGINATION_MISSING_PAGE,
                ErrorConstants.DOCTOR_PAGINATION_INVALID_PAGE
        ));

        JsonNode node = jsonObject.get("perPage");
        if (node != null) {
            if (!node.isInt() || node.asInt() < 10 || node.asInt() > 100) {
                throw UnprocessableEntityException
                        .build()
                        .withReason(ErrorConstants.DOCTOR_PAGINATION_INVALID_PER_PAGE)
                        .getError();
            }

            doctorPagination.setPerPage(node.asInt());
        }

        doctorPagination.setSpecialtyIds(this.getArrayAsInt(
                jsonObject,
                "specialties",
                ErrorConstants.DOCTOR_PAGINATION_INVALID_SPECIALTIES
        ));
        doctorPagination.setLocalityIds(this.getArrayAsInt(
                jsonObject,
                "localities",
                ErrorConstants.DOCTOR_PAGINATION_INVALID_LOCALITIES
        ));

        return doctorPagination;
    }
}
