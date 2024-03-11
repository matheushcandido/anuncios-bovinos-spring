package insetec.backend.controllers;

import insetec.backend.models.Announcement;
import insetec.backend.models.Image;
import insetec.backend.models.User;
import insetec.backend.repositories.AnnouncementRepository;
import insetec.backend.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Announcement> createAnnouncement(@RequestPart("announcement") Announcement announcement, @RequestPart("imageFile") MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        announcement.setUser(currentUser);

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        String imageId = UUID.randomUUID().toString();
        String fileExtension = StringUtils.getFilenameExtension(imageId);

        Path uploadDir = Paths.get("src/main/resources/announcementsImages");
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.copy(imageFile.getInputStream(), uploadDir.resolve(imageId), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image image = new Image();
        image.setId(imageId);
        image.setType(fileExtension);
        image.setAnnouncement(savedAnnouncement);
        savedAnnouncement.getImages().add(image);

        imageRepository.save(image);

        return new ResponseEntity<>(savedAnnouncement, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable String id) {
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        return optionalAnnouncement.map(announcement -> new ResponseEntity<>(announcement, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable String id, @RequestBody Announcement updatedAnnouncement) {
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        if (optionalAnnouncement.isPresent()) {
            Announcement existingAnnouncement = optionalAnnouncement.get();
            existingAnnouncement.setName(updatedAnnouncement.getName());

            Announcement savedAnnouncement = announcementRepository.save(existingAnnouncement);
            return new ResponseEntity<>(savedAnnouncement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable String id) {
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        if (optionalAnnouncement.isPresent()) {
            announcementRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
