package insetec.backend.services;

import insetec.backend.models.Announcement;
import insetec.backend.models.Image;
import insetec.backend.models.User;
import insetec.backend.repositories.AnnouncementRepository;
import insetec.backend.repositories.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ImageRepository imageRepository;

    public Announcement createAnnouncement(Announcement announcement) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        announcement.setUser(currentUser);
        return announcementRepository.save(announcement);
    }

    public Image uploadImage(MultipartFile file, String announcementId) throws IOException {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        File directory = new File("./frontend/src/assets/uploads");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "." + extension;
        Path filePath = Paths.get(directory.getAbsolutePath(), fileName);
        Files.write(filePath, file.getBytes());

        Image image = new Image();
        image.setId(fileName);
        image.setType(file.getContentType());
        image.setAnnouncement(announcement);
        imageRepository.save(image);

        return image;
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementById(String id) {
        return announcementRepository.findById(id);
    }

    public Announcement updateAnnouncement(String id, Announcement updatedAnnouncement) {
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        if (optionalAnnouncement.isPresent()) {
            Announcement existingAnnouncement = optionalAnnouncement.get();
            existingAnnouncement.setName(updatedAnnouncement.getName());
            return announcementRepository.save(existingAnnouncement);
        } else {
            throw new RuntimeException("Announcement not found");
        }
    }

    public void deleteAnnouncement(String id) {
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        if (optionalAnnouncement.isPresent()) {
            announcementRepository.deleteById(id);
        } else {
            throw new RuntimeException("Announcement not found");
        }
    }
}