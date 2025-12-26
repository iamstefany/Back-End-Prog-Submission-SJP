package stefany.piccaro.submission.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stefany.piccaro.submission.entities.Amenity;
import stefany.piccaro.submission.services.AmenityService;

import java.util.List;

@RestController
@RequestMapping("/amenity")
public class AmenityController {

    @Autowired
    private AmenityService amenityService;

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST', 'HOST')")
    @GetMapping("/list")
    public List<Amenity> getAllAmenities() {
        return amenityService.findAll();
    }
}
