package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stefany.piccaro.submission.dto.CreateReviewRequestDTO;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.repositories.ReviewRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    public List<Review> findByUserId(UUID userId) {
        return reviewRepository.findByUser_UserId(userId);
    }

    @Transactional
    public Review save(UUID propertyId, UUID userId, CreateReviewRequestDTO request) {
        User user = userService.findById(userId);
        Property property = propertyService.findById(propertyId);

        Review review = new Review(
                request.rating(),
                request.comment(),
                LocalDateTime.now(),
                user,
                property
        );

        return reviewRepository.save(review);
    }
}