package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
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

    @Column(name = "roles", nullable = false)
    private int roles;


    // ----- Relationships -----
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GuestProfile guestProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HostProfile hostProfile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AdminProfile adminProfile;

    @OneToMany(mappedBy = "user") // 1 User -> N Reviews
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user") // 1 User -> N Bookings
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user") // 1 User -> N Properties
    @JsonIgnore
    private List<Property> properties;


    // ----- Utilities -----
    public boolean hasRole(Role role) { return Role.hasRole(this.roles, role); }
    public void addRole(Role role) { this.roles |= role.getBit(); }
    public void removeRole(Role role) { this.roles &= ~role.getBit(); }


    // ----- Constructors -----
    public User() {}

    public User(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.profileImageUrl = user.getProfileImageUrl();
        this.registrationDate = user.getRegistrationDate();
        this.isBlocked = user.getIsBlocked();
        this.roles = user.getRoles();
    }

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

    public boolean getIsBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public int getRoles() { return roles; }
    public void setRoles(int roles) { this.roles = roles; }

    @Transient
    public List<String> getRoleNames() { return Role.getRoleNames(this.roles); }

    public GuestProfile getGuestProfile() { return guestProfile; }
    public void setGuestProfile(GuestProfile guestProfile) { this.guestProfile = guestProfile; }

    public HostProfile getHostProfile() { return hostProfile; }
    public void setHostProfile(HostProfile hostProfile) { this.hostProfile = hostProfile; }

    public AdminProfile getAdminProfile() { return adminProfile; }
    public void setAdminProfile(AdminProfile adminProfile) { this.adminProfile = adminProfile; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Property> getProperties() { return properties; }
    public void setProperties(List<Property> properties) { this.properties = properties; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "User{" +
                    "userId=" + userId +
                    ", email='" + email + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", profileImageUrl='" + profileImageUrl + '\'' +
                    ", registrationDate='" + registrationDate + '\'' +
                    ", isBlocked='" + isBlocked + '\'' +
                    ", roles=" + roles +
                "}";
    }
}
