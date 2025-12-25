package stefany.piccaro.submission.entities;

public enum Role {
    GUEST(1),
    HOST(2),
    ADMIN(4);

    private final int bit;

    Role(int bit) {
        this.bit = bit;
    }

    public int getBit() {
        return bit;
    }

    // Helper to check if a role is present in a bitmask
    public static boolean hasRole(int rolesBitmask, Role role) {
        return (rolesBitmask & role.getBit()) != 0;
    }
}
