package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "properties")
public class Property {

    // ----- Private static constants -----
    public static final boolean DEFAULT_AUTOMATIC_CONFIRMATION = true;
    public static final LocalTime DEFAULT_CHECK_IN_TIME = LocalTime.of(15, 0); // 3:00 PM
    public static final LocalTime DEFAULT_CHECK_OUT_TIME = LocalTime.of(10, 0); // 10:00 AM


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
    private Integer maxGuests;

    @Column(name = "automatic_confirmation", nullable = false)
    private boolean automaticConfirmation = DEFAULT_AUTOMATIC_CONFIRMATION;

    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime = DEFAULT_CHECK_IN_TIME; // Defaults to 3:00 PM

    @Column(name = "check_out_time", nullable = false)
    private LocalTime checkOutTime = DEFAULT_CHECK_OUT_TIME; // Defaults to 10:00 AM


    // ----- Relationships -----
    @ManyToOne // N Properties -> 1 User
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "property") // 1 Property -> N Bookings
    @JsonIgnore
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true) // 1 Property -> N Reviews
    private Set<Review> reviews;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true) // 1 Property -> N Images
    @JsonIgnore
    private Set<PropertyImage> images = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // N Properties -> N Amenities
    @JoinTable(
            name = "property_amenity",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @JsonIgnore
    private Set<Amenity> amenities = new HashSet<>();


    // ----- Constructors -----
    public Property() {}

    public Property(String title, String description, String address,
                    String city, String country, BigDecimal pricePerNight,
                    Integer maxGuests, Boolean automaticConfirmation,
                    LocalTime checkInTime, LocalTime checkOutTime, User user) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.city = city;
        this.country = country;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;

        // Use defaults if null
        this.automaticConfirmation = automaticConfirmation != null ? automaticConfirmation : DEFAULT_AUTOMATIC_CONFIRMATION;
        this.checkInTime = checkInTime != null ? checkInTime : DEFAULT_CHECK_IN_TIME;
        this.checkOutTime = checkOutTime != null ? checkOutTime : DEFAULT_CHECK_OUT_TIME;
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

    public Integer getMaxGuests() { return maxGuests; }
    public void setMaxGuests(Integer maxGuests) { this.maxGuests = maxGuests; }

    public boolean isAutomaticConfirmation() { return automaticConfirmation; }
    public void setAutomaticConfirmation(boolean automaticConfirmation) { this.automaticConfirmation = automaticConfirmation; }

    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Booking> getBookings() { return bookings; }
    public void addBooking(Booking booking) { bookings.add(booking); booking.setProperty(this); }
    public void removeBooking(Booking booking) { bookings.remove(booking); booking.setProperty(null); }

    public Set<Review> getReviews() { return reviews; }
    public void addReview(Review review) { reviews.add(review); review.setProperty(this); }
    public void removeImage(Review review) { reviews.remove(review); review.setProperty(null); }

    public Set<PropertyImage> getImages() { return images; }
    public void addImage(PropertyImage image) { images.add(image); image.setProperty(this); }
    public void removeImage(PropertyImage image) { images.remove(image); image.setProperty(null); }

    public Set<Amenity> getAmenities() { return amenities; }
    public void addAmenity(Amenity amenity) { this.amenities.add(amenity); }
    public void removeAmenity(Amenity amenity) { this.amenities.remove(amenity); }
    public void clearAmenities() { this.amenities.clear(); }


    // ----- Derived fields -----
    @Transient
    public String getListedBy() {
        if (user == null) {
            return "--";
        }

        return user.getFirstName() + " " + user.getLastName();
    }

    @Transient
    public List<String> getAmenityNames() {
        if (amenities == null || amenities.isEmpty()) {
            return List.of();
        }

        return amenities.stream()
                .map(Amenity::getName)
                .sorted() // optional but nice
                .toList();
    }

    @Transient
    public List<String> getImageUrls() {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                // main image first
                .sorted(Comparator.comparing(PropertyImage::getIsMain).reversed())
                .map(PropertyImage::getImageUrl)
                .toList();
    }

    @Transient
    public int getTotalReviews() {
        return reviews == null ? 0 : reviews.size();
    }

    @Transient
    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(Review::getRating) // rating is int
                .average()
                .orElse(0.0);
    }


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