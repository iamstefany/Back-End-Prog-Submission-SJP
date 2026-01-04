package stefany.piccaro.submission.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stefany.piccaro.submission.entities.Review;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByUser_UserId(UUID userId);

    boolean existsByUser_UserIdAndProperty_PropertyId(UUID userId, UUID propertyId);
}
