package stefany.piccaro.submission.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "card_payments")
public class CardPayment extends Payment {

    // ----- Properties -----
    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardHolder;

    @Column(nullable = false)
    private String expiry;


    // ----- Constructors -----
    public CardPayment() { }

    public CardPayment(BigDecimal amount, Booking booking, String cardNumber, String cardHolder, String expiry) {
        super(amount, booking);
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiry = expiry;
    }


    // ----- Getters/Setters -----
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }

    public String getExpiry() { return expiry; }
    public void setExpiry(String expiry) { this.expiry = expiry; }
}
