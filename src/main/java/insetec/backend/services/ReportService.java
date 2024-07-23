package insetec.backend.services;

import insetec.backend.enums.Status;
import insetec.backend.models.Report;
import insetec.backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public Report createReport(Report report) {
        return repository.save(report);
    }

    public List<Report> getAllReports() {
        return repository.findAll();
    }

    public Optional<Report> getReportById(String id) {
        return repository.findById(id);
    }

    public Optional<Report> closeReport(String id) {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isPresent()) {
            Report existingReport = optionalReport.get();
            existingReport.setStatus(Status.INACTIVE);
            return Optional.of(repository.save(existingReport));
        }
        return Optional.empty();
    }

    public boolean deleteReport(String id) {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}