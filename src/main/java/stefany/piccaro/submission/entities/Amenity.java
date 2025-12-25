package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "amenities")
public class Amenity {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "amenity_id")
    private UUID amenityId;

    @Column(nullable = false)
    private String name;


    // ----- Relationships -----
    @OneToMany(mappedBy = "amenity") // 1 Amenity -> N PropertyAmenities
    @JsonIgnore
    private List<PropertyAmenity> propertyAmenities;


    // ----- Constructors -----
    public Amenity() {}

    public Amenity(String name) {
        this.name = name;
    }


    // ----- Getters/Setters -----
    public UUID getAmenityId() { return amenityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PropertyAmenity> getPropertyAmenities() { return propertyAmenities; }
    public void setPropertyAmenities(List<PropertyAmenity> propertyAmenities) {
        this.propertyAmenities = propertyAmenities;
    }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Amenity{" +
                "amenityId=" + amenityId +
                ", name='" + name + '\'' +
                '}';
    }
}
