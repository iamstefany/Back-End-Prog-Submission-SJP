package stefany.piccaro.submission.services;

import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import stefany.piccaro.submission.dto.CreatePropertyRequestDTO;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.exceptions.NotFoundException;
import stefany.piccaro.submission.repositories.PropertyImageRepository;
import stefany.piccaro.submission.repositories.PropertyRepository;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PropertyService {

    @Autowired
    private UserService userService;
    @Autowired
    private AmenityService amenityService;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${pexels.api.key}")
    private String pexelsApiKey;
    @Autowired
    private PropertyImageRepository propertyImageRepository;

    // Static search terms for image search API call
    private static final String[] SEARCH_TERMS = {
            "house",
            "apartment",
            "villa",
            "hotel",
            "cabin"
    };

    public Property findById(UUID propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException(propertyId));
    }

    @Transactional
    public Property save(UUID userId, CreatePropertyRequestDTO request) {
        User user = userService.findById(userId);

        Property property = new Property(
                request.title(),
                request.description(),
                request.address(),
                request.city(),
                request.country(),
                request.pricePerNight(),
                request.maxGuests(),
                request.automaticConfirmation(),
                request.checkInTime(),
                request.checkOutTime(),
                user
        );

        // Fetch amenities, save in local copy and shuffle
        List<Amenity> amenities = new ArrayList<>(amenityService.findAll());
        Collections.shuffle(amenities);

        // Pick a random number of amenities between 3 and 10 (or total available if less than 10)
        int randomCount = ThreadLocalRandom.current().nextInt(3, Math.min(10, amenities.size() + 1));

        // Add the selected amenities to the property
        for (int i = 0; i < randomCount; i++) {
            property.addAmenity(amenities.get(i));
        }

        // Assign random images from 3rd Party API
        assignRandomImages(property);

        Property saved = propertyRepository.save(property);
        return saved;
    }

    public void assignRandomImages(Property property) {
        // Choose random search terms for variety
        List<String> searchTerms = List.of(SEARCH_TERMS);
        String term = searchTerms.get(ThreadLocalRandom.current().nextInt(searchTerms.size()));

        // Random page 1–5
        int page = ThreadLocalRandom.current().nextInt(1, 6);

        String url = String.format("https://api.pexels.com/v1/search?query=%s&page=%d", term, page);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", pexelsApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Parse JSON to extract image URLs
        JSONObject json = new JSONObject(response.getBody());
        JSONArray photos = json.getJSONArray("photos");

        int numImages = ThreadLocalRandom.current().nextInt(3, 6); // 3–5 images
        for (int i = 0; i < Math.min(numImages, photos.length()); i++) {
            JSONObject photo = photos.getJSONObject(i);
            String imageUrl = photo.getJSONObject("src").getString("original");

            PropertyImage propertyImage = new PropertyImage();
            // Set first image as main
            if (i == 0) {
                propertyImage.setIsMain(true);
            }

            // Set image details
            propertyImage.setProperty(property);
            propertyImage.setImageUrl(imageUrl);

            // Add image to property
            property.addImage(propertyImage);
        }
    }
}