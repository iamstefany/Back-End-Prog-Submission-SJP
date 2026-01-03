package stefany.piccaro.submission.services;

import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import stefany.piccaro.submission.dto.CreatePropertyRequestDTO;
import stefany.piccaro.submission.dto.EditPropertyRequestDTO;
import stefany.piccaro.submission.dto.PropertyCityStatsDTO;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.exceptions.NotFoundException;
import stefany.piccaro.submission.repositories.PropertyImageRepository;
import stefany.piccaro.submission.repositories.PropertyRepository;

import java.math.BigDecimal;
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
    @Autowired
    private PropertyImageRepository propertyImageRepository;
    @Value("${pexels.api.key}")
    private String pexelsApiKey;

    // Static search terms for image search API call
    private static final String[] SEARCH_TERMS = {
            "house",
            "apartment",
            "villa",
            "hotel",
            "cabin"
    };

    // Whitelisted fields for sorting in search endpoint
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "pricePerNight",
            "maxGuests",
            "title"
    );

    public Property findById(UUID propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException(propertyId));
    }

    public List<Property> findByUserId(UUID userId) {
        return propertyRepository.findByUser_UserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Property> search(
            String city,
            String country,
            String amenities,
            Boolean hostVerified,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minGuests,
            String sortBy,
            String direction,
            int page,
            int size
    ) {

        // Parse amenities (comma-separated -> List<String>)
        List<String> amenityNames = null;
        if (amenities != null && !amenities.isBlank()) {
            amenityNames = Arrays.stream(amenities.split(","))
                    .map(String::trim)
                    .toList();
        }

        // Sorting (restrict to whitelisted sorting fields to prevent SQL injection)
        String safeSortBy = ALLOWED_SORT_FIELDS.contains(sortBy)
                ? sortBy
                : "pricePerNight"; // Fallback to pricePerNight

        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                safeSortBy
        );

        // Setup pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        return propertyRepository.search(
                city,
                country,
                amenityNames != null && !amenityNames.isEmpty() ? amenityNames : null,
                amenityNames != null ? amenityNames.size() : 0,
                hostVerified,
                minPrice,
                maxPrice,
                minGuests,
                pageable
        );
    }

    public List<PropertyCityStatsDTO> getStatsByCity() {
        return propertyRepository.getStatsByCity();
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

        // Assign random amenities from database
        assignRandomAmenities(property);

        // Assign random images from 3rd Party API
        assignRandomImages(property);

        return propertyRepository.save(property);
    }

    @Transactional
    public Property editProperty(UUID propertyId, EditPropertyRequestDTO request) {
        Property property = findById(propertyId);

        if (request.title() != null) {
            property.setTitle(request.title());
        }

        if (request.description() != null) {
            property.setDescription(request.description());
        }

        if (request.address() != null) {
            property.setAddress(request.address());
        }

        if (request.pricePerNight() != null) {
            property.setPricePerNight(request.pricePerNight());
        }

        if (request.maxGuests() != null) {
            property.setMaxGuests(request.maxGuests());
        }

        if (request.automaticConfirmation() != null) {
            property.setAutomaticConfirmation(request.automaticConfirmation());
        }

        if (request.checkInTime() != null) {
            property.setCheckInTime(request.checkInTime());
        }

        if (request.checkOutTime() != null) {
            property.setCheckOutTime(request.checkOutTime());
        }

        return propertyRepository.save(property);
    }

    @Transactional
    public void deleteProperty(UUID propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property not found"));

        propertyRepository.delete(property);
    }

    private void assignRandomAmenities(Property property) {
        // Fetch amenities, save in local copy and shuffle
        List<Amenity> amenities = new ArrayList<>(amenityService.findAll());
        Collections.shuffle(amenities);

        // Pick a random number of amenities between 3 and 10 (or total available if less than 10)
        int randomCount = ThreadLocalRandom.current().nextInt(3, Math.min(10, amenities.size() + 1));

        // Add the selected amenities to the property
        for (int i = 0; i < randomCount; i++) {
            property.addAmenity(amenities.get(i));
        }
    }

    private void assignRandomImages(Property property) {
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