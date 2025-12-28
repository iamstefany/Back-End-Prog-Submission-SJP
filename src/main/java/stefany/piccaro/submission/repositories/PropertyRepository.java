package stefany.piccaro.submission.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stefany.piccaro.submission.entities.Property;

import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

}
