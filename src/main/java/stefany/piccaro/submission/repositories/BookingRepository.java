package stefany.piccaro.submission.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stefany.piccaro.submission.entities.Booking;
import stefany.piccaro.submission.entities.Property;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByUser_UserId(UUID userId);

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Booking b
        WHERE b.property.propertyId = :propertyId
        AND b.checkInDate <= :requestedCheckOut
        AND b.checkOutDate >= :requestedCheckIn
    """)
    boolean existsOverlapping(
            @Param("propertyId") UUID propertyId,
            @Param("requestedCheckIn") LocalDate requestedCheckIn,
            @Param("requestedCheckOut") LocalDate requestedCheckOut
    );

    @Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.user.userId = :userId
      AND b.property.propertyId = :propertyId
      AND b.checkOutDate <= CURRENT_DATE
    """)
    boolean existsCompletedBooking(
            @Param("userId") UUID userId,
            @Param("propertyId") UUID propertyId
    );
}