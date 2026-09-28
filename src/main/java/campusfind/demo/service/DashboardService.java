package campusfind.demo.service;

import campusfind.demo.dto.DashboardStats;
import campusfind.demo.entity.FoundStatus;
import campusfind.demo.entity.LostStatus;
import campusfind.demo.entity.Role;
import campusfind.demo.repository.FoundItemRepository;
import campusfind.demo.repository.LostReportRepository;
import campusfind.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final LostReportRepository lostReportRepository;
    private final FoundItemRepository foundItemRepository;

    public DashboardService(UserRepository userRepository,
                            LostReportRepository lostReportRepository,
                            FoundItemRepository foundItemRepository) {
        this.userRepository = userRepository;
        this.lostReportRepository = lostReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public DashboardStats getDashboardStats() {
        long totalUsers = userRepository.count();
        long studentCount = userRepository.countByRole(Role.STUDENT);
        long staffCount = userRepository.countByRole(Role.STAFF);

        long pendingFound = foundItemRepository.countByStatus(FoundStatus.AVAILABLE);
        long claimedFound = foundItemRepository.countByStatus(FoundStatus.CLAIMED);
        long returnedFound = foundItemRepository.countByStatus(FoundStatus.RETURNED);

        long openLost = lostReportRepository.countByStatus(LostStatus.OPEN);
        long resolvedLost = lostReportRepository.countByStatus(LostStatus.RESOLVED);

        return new DashboardStats(
                totalUsers,
                studentCount,
                staffCount,
                pendingFound,
                claimedFound,
                returnedFound,
                openLost,
                resolvedLost
        );
    }
}
