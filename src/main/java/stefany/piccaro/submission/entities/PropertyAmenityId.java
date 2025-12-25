package stefany.piccaro.submission.entities;

import java.io.Serializable;
import java.util.UUID;

public class PropertyAmenityId implements Serializable {

    // ----- Properties -----
    private UUID propertyId;
    private UUID amenityId;


    // ----- Constructors -----
    public PropertyAmenityId() {}

    public PropertyAmenityId(UUID propertyId, UUID amenityId) {
        this.propertyId = propertyId;
        this.amenityId = amenityId;
    }


    // ----- Overrides -----
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyAmenityId that)) return false;
        return propertyId.equals(that.propertyId) && amenityId.equals(that.amenityId);
    }

    @Override
    public int hashCode() {
        return propertyId.hashCode() ^ amenityId.hashCode();
    }
}
