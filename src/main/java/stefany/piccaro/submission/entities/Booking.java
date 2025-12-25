package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    // ----- Properties -----
    @Id
    @GeneratedValue
    private UUID bookingId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDateTime checkOutDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String status;


    // ----- Relationships -----
    @ManyToOne // N bookings -> 1 user (guest)
    @JoinColumn(name = "user_id", nullable = false)
    private User guest;

    @ManyToOne // N bookings -> 1 property
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    private List<Payment> payments;


    // ----- Constructors -----
    public Booking() {
    }

    public Booking(
            LocalDateTime checkInDate,
            LocalDateTime checkOutDate,
            BigDecimal totalPrice,
            String status,
            User guest,
            Property property
    ) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.guest = guest;
        this.property = property;
    }


    // ----- Getters/Setters -----
    public UUID getId() { return bookingId; }

    public LocalDateTime getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDateTime checkInDate) { this.checkInDate = checkInDate; }

    public LocalDateTime getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDateTime checkOutDate) { this.checkOutDate = checkOutDate; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getGuest() { return guest; }
    public void setGuest(User guest) { this.guest = guest; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + bookingId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}
