package stefany.piccaro.submission.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("The resource with ID " + id + " was not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}

