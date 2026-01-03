package stefany.piccaro.submission.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stefany.piccaro.submission.entities.Property;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
    // Corresponds to: SELECT p FROM Property p WHERE p.user.userId = :userId
    List<Property> findByUser_UserId(UUID userId);
}
