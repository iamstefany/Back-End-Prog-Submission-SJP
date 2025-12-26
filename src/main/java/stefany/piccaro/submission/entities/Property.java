package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "properties")
public class Property {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "property_id")
    private UUID propertyId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "max_guests", nullable = false)
    private int maxGuests;

    @Column(name = "automatic_confirmation", nullable = false)
    private boolean automaticConfirmation = true;

    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime = LocalTime.of(15,0);

    @Column(name = "check_out_time", nullable = false)
    private LocalTime checkOutTime = LocalTime.of(10,0);


    // ----- Relationships -----
    @ManyToOne // N Properties -> 1 User
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "property") // 1 Property -> N Bookings
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "property") // 1 Property -> N Reviews
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "property") // 1 Property -> N Reviews
    @JsonIgnore
    private List<PropertyImage> images;

    @OneToMany(mappedBy = "property") // 1 Property -> N PropertyAmenities
    @JsonIgnore
    private List<PropertyAmenity> propertyAmenities;


    // ----- Constructors -----
    public Property() {}

    public Property(String title, String description, String address,
                    String city, String country, BigDecimal pricePerNight,
                    int maxGuests, boolean automaticConfirmation,
                    LocalTime checkInTime, LocalTime checkOutTime, User user) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.city = city;
        this.country = country;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.automaticConfirmation = automaticConfirmation;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.user = user;
    }


    // ----- Getters/Setters -----
    public UUID getPropertyId() { return propertyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public int getMaxGuests() { return maxGuests; }
    public void setMaxGuests(int maxGuests) { this.maxGuests = maxGuests; }

    public boolean isAutomaticConfirmation() { return automaticConfirmation; }
    public void setAutomaticConfirmation(boolean automaticConfirmation) { this.automaticConfirmation = automaticConfirmation; }

    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public List<PropertyImage> getImages() { return images; }
    public void setImages(List<PropertyImage> images) { this.images = images; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Property{" +
                "propertyId=" + propertyId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", maxGuests=" + maxGuests +
                ", automaticConfirmation=" + automaticConfirmation +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", userId=" + (user != null ? user.getUserId() : null) +
                '}';
    }
}