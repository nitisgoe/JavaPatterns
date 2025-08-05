package models.response;

import com.fasterxml.jackson.annotation.JsonCreator;

//Enum for type safety
public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    GUEST("guest"),
    MODERATOR("moderator");
    private final String value;

    UserRole(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserRole fromString(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }

}
