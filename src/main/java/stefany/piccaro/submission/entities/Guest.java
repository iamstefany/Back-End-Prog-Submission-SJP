package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Guest extends User {

    // ----- Properties -----
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;


    // ----- Relationships -----
    @OneToMany(mappedBy = "guest") // 1 User -> N Reviews
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "guest") // 1 User -> N Bookings
    @JsonIgnore
    private List<Booking> bookings;


    // ----- Constructors -----
    public Guest() {
        super();
    }

    public Guest(String email, String password, String firstName, String lastName,
                 String profileImageUrl, LocalDateTime registrationDate, boolean isBlocked, int roles,
                 LocalDate dateOfBirth, String phoneNumber) {
        super(email, password, firstName, lastName, profileImageUrl, registrationDate, isBlocked, roles);
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }


    // ----- Getters/Setters -----
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Guest{" +
                    super.innerPropertiesToString() +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", dateOfBirth='" + dateOfBirth + '\'' +
                "}";
    }
}

