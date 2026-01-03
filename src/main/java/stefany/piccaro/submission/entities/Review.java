package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class Review {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private UUID reviewId;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    // ----- Relationships -----
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;


    // ----- Constructors -----
    public Review() {}

    public Review(int rating, String comment, LocalDateTime createdAt,
                  User user, Property property) {
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.user = user;
        this.property = property;
    }


    // ----- Getters/Setters -----
    public UUID getReviewId() { return reviewId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", userId=" + (user != null ? user.getUserId() : null) +
                ", propertyId=" + (property != null ? property.getPropertyId() : null) +
                '}';
    }
}
