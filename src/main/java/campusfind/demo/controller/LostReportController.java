package campusfind.demo.controller;

import campusfind.demo.dto.CreateLostReportRequest;
import campusfind.demo.entity.LostReport;
import campusfind.demo.entity.LostStatus;
import campusfind.demo.service.LostReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost-reports")
@CrossOrigin(origins = "*")
public class LostReportController {

    private final LostReportService lostReportService;

    public LostReportController(LostReportService lostReportService) {
        this.lostReportService = lostReportService;
    }

    @PostMapping
    public ResponseEntity<LostReport> createLostReport(@Valid @RequestBody CreateLostReportRequest request) {
        LostReport report = lostReportService.createLostReport(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getLostDate(),
                request.getUserId(),
                request.getCategoryId()
        );
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LostReport>> getLostReports(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LostStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate) {
        return ResponseEntity.ok(lostReportService.searchLostReports(categoryId, status, keyword, startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostReport> getLostReportById(@PathVariable Long id) {
        return ResponseEntity.ok(lostReportService.getLostReportById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LostReport>> getLostReportsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(lostReportService.getLostReportsByUserId(userId));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<LostReport> resolveLostReport(@PathVariable Long id) {
        return ResponseEntity.ok(lostReportService.updateStatus(id, LostStatus.RESOLVED));
    }
}
