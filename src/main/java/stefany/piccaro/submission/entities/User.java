package stefany.piccaro.submission.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // For Guest/Host/Admin subclasses
public abstract class User {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    @Column(nullable = false)
    private int roles;


    // ----- Utilities -----
    public boolean hasRole(Role role) { return Role.hasRole(this.roles, role); }
    public void addRole(Role role) { this.roles |= role.getBit(); }
    public void removeRole(Role role) { this.roles &= ~role.getBit(); }


    // ----- Constructors -----
    public User() {}

    public User(String email, String password, String firstName, String lastName,
                String profileImageUrl, LocalDateTime registrationDate, boolean isBlocked, int roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.registrationDate = registrationDate;
        this.isBlocked = isBlocked;
        this.roles = roles;
    }


    // ----- Getters/Setters -----
    public UUID getUserId() { return userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public int getRoles() { return roles; }
    public void setRoles(int roles) { this.roles = roles; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "User{" + innerPropertiesToString() + "}";
    }

    protected String innerPropertiesToString() {
        return "userId=" + userId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", isBlocked='" + isBlocked + '\'' +
                ", roles=" + roles;
    }
}

