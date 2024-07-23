package insetec.backend.controllers;

import insetec.backend.models.Announcement;
import insetec.backend.models.Image;
import insetec.backend.services.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) throws Exception {
        try {
            Announcement savedAnnouncement = announcementService.createAnnouncement(announcement);
            return new ResponseEntity<>(savedAnnouncement, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new Exception("Erro ao criar anúncio.", e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("announcementId") String announcementId) throws IOException {
        try {
            Image image = announcementService.uploadImage(file, announcementId);
            return new ResponseEntity<>(image, HttpStatus.CREATED);
        } catch (IOException e) {
            throw new IOException("Erro ao enviar imagem.", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementService.getAllAnnouncements();
            return new ResponseEntity<>(announcements, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable String id) {
        Optional<Announcement> optionalAnnouncement = announcementService.getAnnouncementById(id);
        return optionalAnnouncement.map(announcement -> new ResponseEntity<>(announcement, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable String id, @RequestBody Announcement updatedAnnouncement) {
        try {
            Announcement savedAnnouncement = announcementService.updateAnnouncement(id, updatedAnnouncement);
            return new ResponseEntity<>(savedAnnouncement, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable String id) {
        try {
            announcementService.deleteAnnouncement(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}