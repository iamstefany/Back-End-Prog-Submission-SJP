package stefany.piccaro.submission.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "paypal_payments")
public class PaypalPayment extends Payment {

    // ----- Properties -----
    @Column(nullable = false)
    private String paypalEmail;

    @Column(nullable = false)
    private String transactionId;


    // ----- Constructors -----
    public PaypalPayment() {}
    public PaypalPayment(String currency,
                         BigDecimal amountCharged,
                         BigDecimal amountEUR,
                         Booking booking,
                         String paypalEmail,
                         String transactionId) {
        super(currency, amountCharged, amountEUR, booking);
        this.paypalEmail = paypalEmail;
        this.transactionId = transactionId;
    }


    // ----- Getters/Setters -----
    public String getPaypalEmail() { return paypalEmail; }
    public void setPaypalEmail(String paypalEmail) { this.paypalEmail = paypalEmail; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
