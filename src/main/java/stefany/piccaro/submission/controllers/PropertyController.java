package stefany.piccaro.submission.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stefany.piccaro.submission.dto.*;
import stefany.piccaro.submission.entities.Property;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.security.JWTTools;
import stefany.piccaro.submission.services.PropertyService;

import java.util.UUID;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PropertyService propertyService;


    // ------- Get property by ID -------
    @GetMapping("/{propertyId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Property getPropertyById(@PathVariable("propertyId") UUID propertyId, HttpServletRequest httpRequest) {
        return propertyService.findById(propertyId);
    }


    // ------- Create property -------
    @PreAuthorize("hasAnyRole('HOST')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Property createProperty(
            HttpServletRequest httpRequest,
            @RequestBody @Validated CreatePropertyRequestDTO request,
            BindingResult validationResult
    ) {
        // Get logged-in user info
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        // Basic DTO validation
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
        }

        // Save user with assigned role
        return propertyService.save(authInfo.userId(), request);
    }
}
