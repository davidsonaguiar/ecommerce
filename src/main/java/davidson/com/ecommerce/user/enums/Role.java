package davidson.com.ecommerce.user.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN(0),
    CLIENT(1);

    private final Integer value;

    Role(Integer value) {
        this.value = value;
    }

    public static Role fromInteger(Integer value) {
        if(value == null) return null;
        for (Role role : Role.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role value: " + value);
    }
}
