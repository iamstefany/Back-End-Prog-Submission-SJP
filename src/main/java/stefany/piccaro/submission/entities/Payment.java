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

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "amount_charged", nullable = false)
    private BigDecimal amountCharged;

    @Column(name = "amount_eur", nullable = false)
    private BigDecimal amountEUR;

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

    public Payment(String currency,
                   BigDecimal amountCharged,
                   BigDecimal amountEUR, Booking booking) {
        this.currency = currency;
        this.amountCharged = amountCharged;
        this.amountEUR = amountEUR;
        this.booking = booking;
    }


    // ----- Getters/Setters -----
    public UUID getPaymentId() { return paymentId; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getAmountCharged() { return amountCharged; }
    public void setAmountCharged(BigDecimal amountCharged) { this.amountCharged = amountCharged; }

    public BigDecimal getAmountEUR() { return amountEUR; }
    public void setAmountEUR(BigDecimal amountEUR) { this.amountEUR = amountEUR; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}