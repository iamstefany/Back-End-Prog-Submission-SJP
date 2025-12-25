package stefany.piccaro.submission.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "property_amenity")
@IdClass(PropertyAmenityId.class)
public class PropertyAmenity {

    // ----- Properties -----
    @Id
    @Column(name = "property_id")
    private UUID propertyId;

    @Id
    @Column(name = "amenity_id")
    private UUID amenityId;


    // ----- Relationships -----
    @ManyToOne // N Properties -> N Amenities
    @JoinColumn(name = "property_id", insertable = false, updatable = false)
    private Property property;

    @ManyToOne // N Amenities -> N Properties
    @JoinColumn(name = "amenity_id", insertable = false, updatable = false)
    private Amenity amenity;


    // ----- Constructors -----
    public PropertyAmenity() {}

    public PropertyAmenity(Property property, Amenity amenity) {
        this.property = property;
        this.amenity = amenity;
        this.propertyId = property.getPropertyId();
        this.amenityId = amenity.getAmenityId();
    }


    // ----- Getters/Setters -----
    public UUID getPropertyId() { return propertyId; }
    public UUID getAmenityId() { return amenityId; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }

    public Amenity getAmenity() { return amenity; }
    public void setAmenity(Amenity amenity) { this.amenity = amenity; }
}
