package stefany.piccaro.submission.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "amenities")
public class Amenity {

    // ----- Properties -----
    @Id
    @GeneratedValue
    @Column(name = "amenity_id")
    private UUID amenityId;

    @Column(name = "name", nullable = false)
    private String name;


    // ----- Constructors -----
    public Amenity() {}

    public Amenity(String name) {
        this.name = name;
    }


    // ----- Getters/Setters -----
    public UUID getAmenityId() { return amenityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    // ----- String Conversion -----
    @Override
    public String toString() {
        return "Amenity{" +
                "amenityId=" + amenityId +
                ", name='" + name + '\'' +
                '}';
    }
}
