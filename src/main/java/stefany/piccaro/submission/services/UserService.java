package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import stefany.piccaro.submission.dto.*;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.exceptions.NotFoundException;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageStorageService imageStorageService;
    @Autowired
    private PasswordEncoder bcrypt;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " was not found."));
    }

    @Transactional
    public SignUpResponseDTO save(SignUpRequestDTO request, Role role) {

        Optional<User> found = userRepository.findByEmail(request.email());

        // Prevent duplicate emails
        if (found.isPresent()) {
            throw new ForbiddenException("User with email " + request.email() + " already exists.");
        }

        // Create new User entity
        User user = new User(request.email(), bcrypt.encode(request.password()),
                request.firstName(), request.lastName(),
                "https://ui-avatars.com/api/?name=" + request.firstName() + "+" + request.lastName(),
                LocalDateTime.now(), false, role.getBit());

        // Create relevant Profile based on role
        switch (role) {
            case GUEST -> {
                GuestProfile guestProfile = new GuestProfile();
                guestProfile.setUserId(user.getUserId());
                guestProfile.setUser(user);
                guestProfile.setDateOfBirth(request.dateOfBirth());
                guestProfile.setPhoneNumber(request.phoneNumber());

                user.setGuestProfile(guestProfile);
            }
            case HOST -> {
                HostProfile hostProfile = new HostProfile();
                hostProfile.setUserId(user.getUserId());
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

    public String updateProfileImage(UUID userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("File cannot be empty.");
        }

        User user = findById(userId);

        // Upload image (local / cloud / third-party API)
        String imageUrl = imageStorageService.upload(file);

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    @Transactional
    public User editUser(UUID userId, EditUserRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }

        if (request.dateOfBirth() != null || request.phoneNumber() != null) {

            GuestProfile guestProfile = user.getGuestProfile();

            if (guestProfile == null) {
                throw new ForbiddenException("Only guests can edit date of birth or phone number");
            }

            if (request.dateOfBirth() != null) {
                guestProfile.setDateOfBirth(request.dateOfBirth());
            }

            if (request.phoneNumber() != null) {
                guestProfile.setPhoneNumber(request.phoneNumber());
            }
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Transactional
    public User blockUser(User user) {
        user.setBlocked(true);
        return userRepository.save(user);
    }

    @Transactional
    public User unblockUser(User user) {
        user.setBlocked(false);
        return userRepository.save(user);
    }

    @Transactional
    public User createAdmin(CreateAdminRequestDTO request) {

        Optional<User> found = userRepository.findByEmail(request.email());

        // Prevent duplicate emails
        if (found.isPresent()) {
            throw new ForbiddenException("User with email " + request.email() + " already exists.");
        }

        // Create new User entity
        User user = new User(request.email(), bcrypt.encode(request.password()),
                request.firstName(), request.lastName(),
                "https://ui-avatars.com/api/?name=" + request.firstName() + "+" + request.lastName(),
                LocalDateTime.now(), false, Role.ADMIN.getBit());

        // Create admin profile
        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setUserId(user.getUserId());
        adminProfile.setUser(user);
        adminProfile.setIsSuperAdmin(request.isSuperAdmin());
        user.setAdminProfile(adminProfile);

        // Add user
        return userRepository.save(user);
    }

    @Transactional
    public void resetPassword(UUID userId, EditPasswordRequestDTO request) {
        User user = findById(userId);
        user.setPassword(bcrypt.encode(request.newPassword()));
        userRepository.save(user);
    }
}