package com.myblog.utility;

public class UserRole {
    public static final String ADMIN = "ADMIN";
    public static final String CLOSE_FRIEND = "CLOSE_FRIEND";
    public static final String FRIEND = "FRIEND";
    public static final String GUEST = "GUEST";

    public static int getLevel(String role) {
        switch (role) {
            case ADMIN:
                return 4;
            case CLOSE_FRIEND:
                return 3;
            case FRIEND:
                return 2;
            case GUEST:
            default:
                return 1;
        }
    }

    public static boolean hasPermission(String userRole, String requiredRole) {
        return getLevel(userRole) >= getLevel(requiredRole);
    }

    public static boolean hasPermission(String userRole, int requiredLevel) {
        return getLevel(userRole) >= requiredLevel;
    }
}
