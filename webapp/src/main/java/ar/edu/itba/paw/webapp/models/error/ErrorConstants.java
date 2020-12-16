package ar.edu.itba.paw.webapp.models.error;

// TODO: Guido i18n
public abstract class ErrorConstants {
    public static final APISubError APPOINTMENT_CREATE_MISSING_DATE_FROM
            = missingField(1, "date_from");
    public static final APISubError APPOINTMENT_CREATE_INVALID_DATE_FROM
            = invalidField(2, "date_from");
    public static final APISubError APPOINTMENT_CREATE_INVALID_MOTIVE
            = invalidField(3, "motive");
    public static final APISubError APPOINTMENT_CREATE_INVALID_MESSAGE
            = invalidField(4, "message");
    public static final APISubError APPOINTMENT_CREATE_MISSING_DOCTOR_ID
            = missingField(5, "doctorId");
    public static final APISubError APPOINTMENT_CREATE_INVALID_DOCTOR_ID
            = invalidField(5, "doctorId");
    public static final APISubError APPOINTMENT_CREATE_NONEXISTENT_DOCTOR
            = nonExistentEntity(5, "doctor", "doctorId");

    public static final APISubError APPOINTMENT_TIME_SLOT_GET_NONEXISTENT_DOCTOR
            = nonExistentEntity(5, "doctor", "doctor_id");

    public static final APISubError LOCALITY_GET_NONEXISTENT_PROVINCE
            = nonExistentEntity(5, "province", "provinceId");

    public static final APISubError PROVINCE_GET_NONEXISTENT_COUNTRY
            = nonExistentEntity(5, "country", "countryId");

    public static final APISubError DOCTOR_PAGINATION_INVALID_NAME
            = invalidField(5, "name");
    public static final APISubError DOCTOR_PAGINATION_MISSING_PAGE
            = missingField(5, "page");
    public static final APISubError DOCTOR_PAGINATION_INVALID_PAGE
            = invalidField(5, "page");
    public static final APISubError DOCTOR_PAGINATION_INVALID_PER_PAGE
            = invalidField(5, "per_page");
    public static final APISubError DOCTOR_PAGINATION_INVALID_SPECIALTIES
            = invalidField(5, "specialties");
   public static final APISubError DOCTOR_PAGINATION_INVALID_LOCALITIES
            = invalidField(5, "localities");

    public static final APISubError DOCTOR_UPDATE_INVALID_PHONE
            = invalidField(5, "phone");
    public static final APISubError DOCTOR_UPDATE_INVALID_EMAIL
            = invalidField(5, "email");
    public static final APISubError DOCTOR_UPDATE_INVALID_SPECIALTIES
            = invalidField(5, "specialtyIds");
    public static final APISubError DOCTOR_UPDATE_SOME_INVALID_SPECIALTIES
            = new APISubError(5, "Some of the specialties provided could not be found");
    public static final APISubError DOCTOR_UPDATE_EMPTY_SPECIALTIES
            = new APISubError(5, "You need to provide at least one specialty");

    public static final APISubError USER_CREATE_MISSING_EMAIL
            = missingField(5, "email");
    public static final APISubError USER_CREATE_INVALID_EMAIL
            = invalidField(5, "email");
    public static final APISubError USER_CREATE_MISSING_FIRST_NAME
            = missingField(5, "firstName");
    public static final APISubError USER_CREATE_INVALID_FIRST_NAME
            = invalidField(5, "firstName");
    public static final APISubError USER_CREATE_MISSING_SURNAME
            = missingField(5, "surname");
    public static final APISubError USER_CREATE_INVALID_SURNAME
            = invalidField(5, "surname");
    public static final APISubError USER_CREATE_MISSING_PASSWORD
            = missingField(5, "password");
    public static final APISubError USER_CREATE_INVALID_PASSWORD
            = invalidField(5, "password");
    public static final APISubError USER_CREATE_INVALID_PHONE
            = invalidField(5, "phone");

    public static final APISubError USER_CREATE_DOCTOR_INVALID_REGISTRATION_NUMBER
            = invalidField(5, "registrationNumber");
    public static final APISubError USER_CREATE_DOCTOR_MISSING_LOCALITY_ID
            = invalidField(5, "localityId");
    public static final APISubError USER_CREATE_DOCTOR_INVALID_LOCALITY_ID
            = invalidField(5, "localityId");
    public static final APISubError USER_CREATE_NONEXISTENT_LOCALITY
            = nonExistentEntity(5, "Locality", "localityId");
    public static final APISubError USER_CREATE_DOCTOR_MISSING_STREET
            = invalidField(5, "street");
    public static final APISubError USER_CREATE_DOCTOR_INVALID_STREET
            = invalidField(5, "street");

    public static final APISubError USER_UPDATE_INVALID_EMAIL
            = invalidField(5, "email");
    public static final APISubError USER_UPDATE_INVALID_FIRST_NAME
            = invalidField(5, "firstName");
    public static final APISubError USER_UPDATE_INVALID_SURNAME
            = invalidField(5, "surname");
    public static final APISubError USER_UPDATE_INVALID_PHONE
            = invalidField(5, "phone");

    public static final APISubError WORKDAY_CREATE_INVALID_DAY
            = invalidField(5, "day");
    public static final APISubError WORKDAY_CREATE_MISSING_DAY
            = invalidField(5, "day");
    public static final APISubError WORKDAY_CREATE_MISSING_START
            = invalidField(5, "start");
    public static final APISubError WORKDAY_CREATE_INVALID_START
            = invalidField(5, "start");
    public static final APISubError WORKDAY_CREATE_MISSING_END
            = invalidField(5, "end");
    public static final APISubError WORKDAY_CREATE_INVALID_END
            = invalidField(5, "end");
    public static final APISubError WORKDAY_CREATE_MISSING_START_HOUR
            = invalidField(5, "start.hour");
    public static final APISubError WORKDAY_CREATE_INVALID_START_HOUR
            = invalidField(5, "start.hour");
    public static final APISubError WORKDAY_CREATE_MISSING_START_MINUTE
            = invalidField(5, "start.minute");
    public static final APISubError WORKDAY_CREATE_INVALID_START_MINUTE
            = invalidField(5, "start.minute");
    public static final APISubError WORKDAY_CREATE_MISSING_END_HOUR
            = invalidField(5, "end.hour");
    public static final APISubError WORKDAY_CREATE_INVALID_END_HOUR
            = invalidField(5, "end.hour");
    public static final APISubError WORKDAY_CREATE_MISSING_END_MINUTE
            = invalidField(5, "end.minute");
    public static final APISubError WORKDAY_CREATE_INVALID_END_MINUTE
            = invalidField(5, "end.minute");

    public static final APISubError DATE_RANGE_TOO_BROAD
            = new APISubError(5, "The selected range date is too broad");
    public static final APISubError DATE_FROM_IS_AFTER_TO
            = new APISubError(5, "The start date is after the end one");

    public static final APISubError MISSING_BODY_PARAMS
            = new APISubError(5, "Some of the required body parameters are missing");
    public static final APISubError MISSING_PATH_PARAMS
            = new APISubError(5, "Some of the required path parameters are missing or are invalid");
    public static final APISubError MISSING_QUERY_PARAMS
            = new APISubError(5, "Some of the required query parameters are missing");
    public static final APISubError INVALID_QUERY_PARAMS
            = new APISubError(5, "Some of the passed query parameters are invalid");

    public static final APISubError USER_EMAIL_USED
            = new APISubError(5, "The email specified is already being used");


    private static APISubError invalidField(int code, String field) {
        return new APISubError(code, "Invalid \"" + field + "\" field");
    }

    private static APISubError missingField(int code, String field) {
        return new APISubError(code, "Missing \"" + field + "\" field");
    }

    private static APISubError nonExistentEntity(int code, String entityName, String field) {
        return new APISubError(
                code,
                "Provided id for \""
                        + entityName +
                        "\" entity in field \""
                        + field +
                        "\" does not exist"
        );
    }
}
