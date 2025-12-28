package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "property_images")
public class PropertyImage {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "property_image_id")
    private UUID propertyImageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_main", nullable = false)
    private boolean isMain = false;


    // ----- Relationship -----
    @ManyToOne // N Images -> 1 Property
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;


    // ----- Constructors -----
    public PropertyImage() {}

    public PropertyImage(String imageUrl, boolean isMain, Property property) {
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.property = property;
    }


    // ----- Getters/Setters -----
    public UUID getPropertyImageId() { return propertyImageId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean getIsMain() { return isMain; }
    public void setIsMain(boolean main) { isMain = main; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "PropertyImage{" +
                "propertyImageId=" + propertyImageId +
                ", imageUrl='" + imageUrl + '\'' +
                ", isMain=" + isMain +
                ", propertyId=" + (property != null ? property.getPropertyId() : null) +
                '}';
    }
}
