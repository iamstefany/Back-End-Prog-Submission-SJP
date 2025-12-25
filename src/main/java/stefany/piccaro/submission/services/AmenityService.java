package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stefany.piccaro.submission.entities.Amenity;
import stefany.piccaro.submission.repositories.AmenityRepository;

import java.util.List;

@Service
public class AmenityService {

    @Autowired
    private AmenityRepository amenityRepository;

    public List<Amenity> findAll() {
        return amenityRepository.findAll();
    }
}