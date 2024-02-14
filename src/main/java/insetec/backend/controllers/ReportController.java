package insetec.backend.controllers;

import insetec.backend.models.Announcement;
import insetec.backend.models.Report;
import insetec.backend.repositories.AnnouncementRepository;
import insetec.backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportRepository repository;

    @PostMapping
    public ResponseEntity<Report> create(@RequestBody Report report) {
        Report savedReport = repository.save(report);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAll() {
        List<Report> reports = repository.findAll();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable String id) {
        Optional<Report> optionalReport = repository.findById(id);
        return optionalReport.map(report -> new ResponseEntity<>(report, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
