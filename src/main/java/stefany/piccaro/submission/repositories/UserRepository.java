package stefany.piccaro.submission.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import stefany.piccaro.submission.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    // Examples of raw queries
//    @Query("SELECT COUNT(b) FROM Blog b WHERE b.author.id = :userId")
//    Long countBlogsByUser(UUID userId);
//
//    @Query("SELECT COUNT(b) FROM Blog b WHERE b.author = :user")
//    Long countBlogsByUser(User user);

}
