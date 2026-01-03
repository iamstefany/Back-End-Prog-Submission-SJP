package stefany.piccaro.submission.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
public class Payment {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    private String status;


    // ----- Relationship -----
    @ManyToOne // N Payments -> 1 Booking
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;


    // ----- Constructors -----
    public Payment() {}

    public Payment(BigDecimal amount, Booking booking) {
        this.amount = amount;
        this.booking = booking;
    }


    // ----- Getters/Setters -----
    public UUID getPaymentId() { return paymentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}