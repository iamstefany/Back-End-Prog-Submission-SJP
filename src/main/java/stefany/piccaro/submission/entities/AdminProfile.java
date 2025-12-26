package stefany.piccaro.submission.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_profiles")
public class AdminProfile {

    // ----- Properties -----
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "is_super_admin")
    private boolean isSuperAdmin;


    // ----- Relationships -----
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false) // 1 AdminProfile -> 1 User
    private User user;


    // ----- Constructors -----
    public AdminProfile() {
        super();
    }

    public AdminProfile(boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }


    // ----- Getters/Setters -----
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public boolean getIsSuperAdmin() { return isSuperAdmin; }
    public void setIsSuperAdmin(boolean isSuperAdmin) { this.isSuperAdmin = isSuperAdmin; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Admin{" +
                    "isSuperAdmin='" + isSuperAdmin + '\'' +
                "}";
    }
}
