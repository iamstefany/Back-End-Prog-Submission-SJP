package stefany.piccaro.submission.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "uploads/";

    public String upload(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Extract extension from original filename
            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new RuntimeException("Invalid file name");
            }
            String extension = originalName.substring(originalName.lastIndexOf('.'));

            // Assign random filename
            String fileName = UUID.randomUUID() + extension;

            // Save file
            Path path = Paths.get(UPLOAD_DIR, fileName);
            Files.write(path, file.getBytes());

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
}
