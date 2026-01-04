package stefany.piccaro.submission.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stefany.piccaro.submission.dto.AuthInfoDTO;
import stefany.piccaro.submission.entities.Booking;
import stefany.piccaro.submission.entities.Role;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.security.JWTTools;
import stefany.piccaro.submission.services.BookingService;
import stefany.piccaro.submission.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private BookingService bookingService;


    // ------- Get booking by ID -------
    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Booking getBookingById(@PathVariable("bookingId") UUID bookingId, HttpServletRequest httpRequest) {
        Booking booking = bookingService.findById(bookingId);

        // Get auth info from request
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        // Protect booking access depending on role (only Admins can view all bookings)
        if (!Role.hasRole(authInfo.roles(), Role.ADMIN)) {
            // A guest may only view their own bookings
            if (Role.hasRole(authInfo.roles(), Role.GUEST)) {
                if (!booking.getUser().getUserId().equals(authInfo.userId())) {
                    throw new ForbiddenException("You are not authorized to view this booking, as it does not belong to you.");
                }
            }

            // A host may only view bookings for their own properties
            if (Role.hasRole(authInfo.roles(), Role.HOST)) {
                if (!booking.getProperty().getUser().getUserId().equals(authInfo.userId())) {
                    throw new ForbiddenException("You are not authorized to view this booking, as it is not for one of your properties.");
                }
            }
        }

        return booking;
    }


    // ------- Get my bookings -------
    @PreAuthorize("hasAnyRole('GUEST')")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Booking> getMyBookings(HttpServletRequest httpRequest) {
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);
        return bookingService.findByUserId(authInfo.userId());
    }


    // ------- Approve a booking -------
    @PreAuthorize("hasAnyRole('HOST')")
    @PostMapping("/{bookingId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public Booking approveBooking(
            @PathVariable("bookingId") UUID bookingId,
            HttpServletRequest httpRequest) {
        // Get logged-in user info
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        return bookingService.approveBooking(bookingId, authInfo.userId());
    }


    // ------- Reject a booking -------
    @PreAuthorize("hasAnyRole('HOST')")
    @PostMapping("/{bookingId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectBooking(
            @PathVariable("bookingId") UUID bookingId,
            HttpServletRequest httpRequest) {
        // Get logged-in user info
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        bookingService.rejectBooking(bookingId, authInfo.userId());
    }
}