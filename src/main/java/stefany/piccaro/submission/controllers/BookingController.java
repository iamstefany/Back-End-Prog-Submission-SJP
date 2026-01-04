package stefany.piccaro.submission.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stefany.piccaro.submission.dto.AuthInfoDTO;
import stefany.piccaro.submission.entities.Booking;
import stefany.piccaro.submission.security.JWTTools;
import stefany.piccaro.submission.services.BookingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private BookingService bookingService;


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