package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "host_profiles")
public class HostProfile {

    // ----- Properties -----
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "host_since")
    private LocalDate hostSince;

    @Column(name = "host_verified", nullable = false)
    private boolean hostVerified = false;


    // ----- Relationships -----
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false) // 1 HostProfile -> 1 User
    private User user;


    // ----- Constructors -----
    public HostProfile() {
        super();
    }

    public HostProfile(LocalDate hostSince, boolean hostVerified) {
        this.hostSince = hostSince;
        this.hostVerified = hostVerified;
    }


    // ----- Getters/Setters -----
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public LocalDate getHostSince() { return hostSince; }
    public void setHostSince(LocalDate hostSince) { this.hostSince = hostSince; }

    public boolean isHostVerified() { return hostVerified; }
    public void setHostVerified(boolean hostVerified) { this.hostVerified = hostVerified; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Host{" +
                    "hostSince='" + hostSince + '\'' +
                    ", hostVerified='" + hostVerified + '\'' +
                "}";
    }
}
