package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Host extends User {

    // ----- Properties -----
    @Column(name = "host_since")
    private LocalDate hostSince;

    @Column(name = "host_verified", nullable = false)
    private boolean hostVerified = false;


    // ----- Relationships -----
    @OneToMany(mappedBy = "host") // 1 Host -> N Properties
    @JsonIgnore
    private List<Property> properties;


    // ----- Constructors -----
    public Host() {
        super();
    }

    public Host(String email, String password, String firstName, String lastName,
                String profileImageUrl, LocalDateTime registrationDate, boolean isBlocked, int roles,
                LocalDate hostSince, boolean hostVerified) {
        super(email, password, firstName, lastName, profileImageUrl, registrationDate, isBlocked, roles);
        this.hostSince = hostSince;
        this.hostVerified = hostVerified;
    }


    // ----- Getters/Setters -----
    public LocalDate getHostSince() { return hostSince; }
    public void setHostSince(LocalDate hostSince) { this.hostSince = hostSince; }

    public boolean isHostVerified() { return hostVerified; }
    public void setHostVerified(boolean hostVerified) { this.hostVerified = hostVerified; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Host{" +
                    super.innerPropertiesToString() +
                    ", hostSince='" + hostSince + '\'' +
                    ", hostVerified='" + hostVerified + '\'' +
                "}";
    }
}
