package stefany.piccaro.submission.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stefany.piccaro.submission.dto.PropertyCityStatsDTO;
import stefany.piccaro.submission.entities.Property;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

    // Corresponds to: SELECT p FROM Property p WHERE p.user.userId = :userId
    List<Property> findByUser_UserId(UUID userId);

    @Query("""
        SELECT p
        FROM Property p
        JOIN p.user u
        LEFT JOIN p.amenities a
        WHERE (:city IS NULL OR LOWER(p.city) = :city)
        AND (:country IS NULL OR LOWER(p.country) = :country)
        AND (:hostVerified IS NULL OR u.hostProfile.hostVerified = :hostVerified)
        AND (:minPrice IS NULL OR p.pricePerNight >= :minPrice)
        AND (:maxPrice IS NULL OR p.pricePerNight <= :maxPrice)
        AND (:minGuests IS NULL OR p.maxGuests >= :minGuests)
        GROUP BY p
        HAVING (
            :amenities IS NULL OR
            COUNT(DISTINCT CASE
                WHEN LOWER(a.name) IN :amenities THEN a.id
            END) = :numAmenities
        )
    """)
    Page<Property> search(
            @Param("city") String city,
            @Param("country") String country,
            @Param("amenities") List<String> amenities,
            @Param("numAmenities") long numAmenities,
            @Param("hostVerified") Boolean hostVerified,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minGuests") Integer minGuests,
            Pageable pageable
    );

    @Query("""
    SELECT new stefany.piccaro.submission.dto.PropertyCityStatsDTO(
        p.city,
        COUNT(p),
        AVG(p.pricePerNight),
        MIN(p.pricePerNight),
        MAX(p.pricePerNight)
    )
    FROM Property p
    GROUP BY p.city
    """)
    List<PropertyCityStatsDTO> getStatsByCity();
}
