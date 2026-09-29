package campusfind.demo.service;

import campusfind.demo.entity.Category;
import campusfind.demo.entity.LostReport;
import campusfind.demo.entity.LostStatus;
import campusfind.demo.entity.User;
import campusfind.demo.exception.BadRequestException;
import campusfind.demo.exception.ResourceNotFoundException;
import campusfind.demo.repository.CategoryRepository;
import campusfind.demo.repository.LostReportRepository;
import campusfind.demo.repository.UserRepository;
import campusfind.demo.specification.LostReportSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LostReportService {

    private final LostReportRepository lostReportRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public LostReportService(LostReportRepository lostReportRepository,
                             UserRepository userRepository,
                             CategoryRepository categoryRepository) {
        this.lostReportRepository = lostReportRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public LostReport createLostReport(String title, String description, String location,
                                       LocalDate lostDate, Long userId, Long categoryId) {
        if (lostDate != null && lostDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Lost date cannot be in the future: " + lostDate);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        LostReport report = new LostReport();
        report.setTitle(title != null ? title.trim() : "");
        report.setDescription(description != null ? description.trim() : "");
        report.setLocation(location != null ? location.trim() : "");
        report.setLostDate(lostDate != null ? lostDate : LocalDate.now());
        report.setUser(user);
        report.setCategory(category);
        report.setStatus(LostStatus.OPEN);

        return lostReportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<LostReport> getAllLostReports() {
        return lostReportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LostReport getLostReportById(Long id) {
        return lostReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LostReport not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<LostReport> getLostReportsByUserId(Long userId) {
        return lostReportRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<LostReport> getOpenLostReports() {
        return lostReportRepository.findByStatus(LostStatus.OPEN);
    }

    @Transactional(readOnly = true)
    public List<LostReport> searchLostReports(Long categoryId, LostStatus status, String keyword, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("startDate cannot be after endDate.");
        }
        return lostReportRepository.findAll(LostReportSpecification.filter(categoryId, status, keyword, startDate, endDate));
    }

    public LostReport updateStatus(Long id, LostStatus newStatus) {
        LostReport report = getLostReportById(id);
        report.setStatus(newStatus);
        return lostReportRepository.save(report);
    }
}
