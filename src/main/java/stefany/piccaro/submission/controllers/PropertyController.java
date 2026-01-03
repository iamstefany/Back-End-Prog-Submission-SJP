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
import stefany.piccaro.submission.entities.Role;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.security.JWTTools;
import stefany.piccaro.submission.services.PropertyService;

import java.util.List;
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


    // ------- Get my properties -------
    @PreAuthorize("hasAnyRole('HOST')")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Property> getMyProperties(HttpServletRequest httpRequest) {
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);
        return propertyService.findByUserId(authInfo.userId());
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


    // ------- Edit property -------
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PatchMapping("/{propertyId}")
    @ResponseStatus(HttpStatus.OK)
    public Property editProperty(
            @PathVariable("propertyId") UUID propertyId,
            @RequestBody @Validated EditPropertyRequestDTO request,
            HttpServletRequest httpRequest,
            BindingResult validationResult
    ) {
        // Get logged-in user info
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        // If you're not an admin, you're not allowed to delete others' properties
        Property property = propertyService.findById(propertyId);
        if (!Role.hasRole(authInfo.roles(), Role.ADMIN)) {
            if (!property.getUser().getUserId().equals(authInfo.userId())) {
                throw new ForbiddenException("Access denied: insufficient permissions to modify this property. You are not the owner.");
            }
        }

        // Basic DTO validation
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
        }

        // Edit property
        return propertyService.editProperty(propertyId, request);
    }


    // ------- Delete property -------
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @DeleteMapping("/{propertyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProperty(
            @PathVariable("propertyId") UUID propertyId,
            HttpServletRequest httpRequest
    ) {
        // Get authInfo and property info
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        // If you're not an admin, you're not allowed to delete other users' properties
        Property property = propertyService.findById(propertyId);
        if (!Role.hasRole(authInfo.roles(), Role.ADMIN)) {
            if (!property.getUser().getUserId().equals(authInfo.userId())) {
                throw new ForbiddenException("Access denied: insufficient permissions to delete this property. You are not the owner.");
            }
        }

        propertyService.deleteProperty(propertyId);
    }
}
