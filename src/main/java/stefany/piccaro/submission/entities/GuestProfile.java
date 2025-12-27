package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "guest_profiles")
public class GuestProfile {

    // ----- Properties -----
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;


    // ----- Relationships -----
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false) // 1 GuestProfile -> 1 User
    @JsonIgnore
    private User user;


    // ----- Constructors -----
    public GuestProfile() {
        super();
    }

    public GuestProfile(LocalDate dateOfBirth, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }


    // ----- Getters/Setters -----
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Guest{" +
                    "phoneNumber='" + phoneNumber + '\'' +
                    ", dateOfBirth='" + dateOfBirth + '\'' +
                "}";
    }
}

