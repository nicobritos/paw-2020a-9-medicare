package ar.edu.itba.paw.webapp.auth;

public enum UserRole {
    PATIENT,
    STAFF;

    public static String[] names() {
        String[] roles = new String[UserRole.values().length];
        int i = 0;
        for (UserRole userRole : UserRole.values()) {
            roles[i++] = userRole.name();
        }
        return roles;
    }
}
