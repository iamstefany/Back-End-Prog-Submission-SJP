package stefany.piccaro.submission.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Admin extends User {

    // ----- Properties -----
    @Column(name = "is_super_admin")
    private boolean isSuperAdmin;


    // ----- Constructors -----
    public Admin() {
        super();
    }

    public Admin(String email, String password, String firstName, String lastName,
                String profileImageUrl, LocalDateTime registrationDate, boolean isBlocked, int roles,
                boolean isSuperAdmin) {
        super(email, password, firstName, lastName, profileImageUrl, registrationDate, isBlocked, roles);
        this.isSuperAdmin = isSuperAdmin;
    }


    // ----- Getters/Setters -----
    public boolean getIsSuperAdmin() { return isSuperAdmin; }
    public void setIsSuperAdmin(boolean isSuperAdmin) { this.isSuperAdmin = isSuperAdmin; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Admin{" +
                    super.innerPropertiesToString() +
                    ", isSuperAdmin='" + isSuperAdmin + '\'' +
                "}";
    }
}
