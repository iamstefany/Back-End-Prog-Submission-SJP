package stefany.piccaro.submission.entities;

import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    GUEST(1),
    HOST(2),
    ADMIN(4);

    private final int bit;

    // Constructor to initialize the bit value
    Role(int bit) {
        this.bit = bit;
    }

    // Get the bit value associated with the role
    public int getBit() {
        return bit;
    }

    // Helper to check if a role is present in a bitmask
    public static boolean hasRole(int rolesBitmask, Role role) {
        return (rolesBitmask & role.getBit()) != 0;
    }

    public static List<String> getRoleNames(int roles) {
        List<String> roleNames = new ArrayList<>();

        for (Role role : Role.values()) {
            if ((roles & role.getBit()) != 0) {
                roleNames.add(role.name());
            }
        }

        return roleNames;
    }
}
