package stefany.piccaro.submission.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public String check() {
        return "OK"; // Just checking if the API is reachable
    }

    @GetMapping("/500")
    public String error500() {
        int a = 1 / 0; // This will cause a division by zero error
        return "OK";
    }
}
