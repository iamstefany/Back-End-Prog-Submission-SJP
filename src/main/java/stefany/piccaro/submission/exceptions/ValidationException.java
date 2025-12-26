package stefany.piccaro.submission.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errorsList;

    public ValidationException(String message) {
        super("Validation errors for payload: " + message);
    }

    public ValidationException(List<String> errorsList) {
        super("Validation errors for payload: " + String.join("; ", errorsList));
        this.errorsList = errorsList;
    }

    public List<String> getErrorsList() {
        return errorsList;
    }
}

