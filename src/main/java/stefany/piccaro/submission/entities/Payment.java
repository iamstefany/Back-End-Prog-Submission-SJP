package stefany.piccaro.submission.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;


    // ----- Relationship -----
    @ManyToOne // N Payments -> 1 Booking
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;


    // ----- Constructors -----
    public Payment() {}

    public Payment(BigDecimal amount, String paymentMethod, String paymentStatus,
                   LocalDateTime paymentDate, Booking booking) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.booking = booking;
    }


    // ----- Getters/Setters -----
    public UUID getPaymentId() { return paymentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}