package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stefany.piccaro.submission.dto.SignUpRequestDTO;
import stefany.piccaro.submission.dto.SignUpResponseDTO;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.exceptions.NotFoundException;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " was not found."));
    }

    public SignUpResponseDTO save(SignUpRequestDTO request, Role role) {

        Optional<User> found = userRepository.findByEmail(request.email());

        User user;

        // New user
        if (found.isEmpty()) {
            user = new User(request.email(), bcrypt.encode(request.password()),
                    request.firstName(), request.lastName(),
                    "https://ui-avatars.com/api/?name=" + request.firstName() + "+" + request.lastName(),
                    LocalDateTime.now(), false, role.getBit());
        }
        // Existing user - we'll update fields and upgrade their roles
        else {
            user = found.get();
            user.setEmail(request.email());
            user.setPassword(bcrypt.encode(request.password()));
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setRoles(user.getRoles() | role.getBit());
        }

        switch (role) {
            case GUEST -> {
                GuestProfile guestProfile = new GuestProfile();
                guestProfile.setUser(user);
                guestProfile.setDateOfBirth(request.dateOfBirth());
                guestProfile.setPhoneNumber(request.phoneNumber());

                user.setGuestProfile(guestProfile);
            }
            case HOST -> {
                HostProfile hostProfile = new HostProfile();
                hostProfile.setUser(user);
                hostProfile.setHostSince(LocalDate.now());
                hostProfile.setHostVerified(false); // Host not verified by default, admins can verify later

                user.setHostProfile(hostProfile);
            }
            default -> {
                throw new ValidationException("Invalid role specified."); // Admin and other roles are not accepted here
            }
        }

        // Update host/guest profile info and return User ID
        User saved = userRepository.save(user);
        return new SignUpResponseDTO(user.getUserId());
    }
}