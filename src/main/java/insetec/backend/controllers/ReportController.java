package insetec.backend.controllers;

import insetec.backend.models.Report;
import insetec.backend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<Report> create(@RequestBody Report report) {
        try {
            Report savedReport = reportService.createReport(report);
            return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAll() {
        try {
            List<Report> reports = reportService.getAllReports();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable String id) {
        Optional<Report> optionalReport = reportService.getReportById(id);
        return optionalReport.map(report -> new ResponseEntity<>(report, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> close(@PathVariable String id) {
        Optional<Report> optionalReport = reportService.closeReport(id);
        return optionalReport.map(report -> new ResponseEntity<>(report, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (reportService.deleteReport(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}