package campusfind.demo;

import campusfind.demo.config.DataInitializer;
import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import campusfind.demo.entity.LostReport;
import campusfind.demo.entity.LostStatus;
import campusfind.demo.entity.Role;
import campusfind.demo.entity.User;
import campusfind.demo.dto.DashboardStats;
import campusfind.demo.dto.ItemMatch;
import campusfind.demo.exception.BadRequestException;
import campusfind.demo.exception.UnauthorizedException;
import campusfind.demo.repository.FoundItemRepository;
import campusfind.demo.repository.LostReportRepository;
import campusfind.demo.repository.UserRepository;
import campusfind.demo.service.CategoryService;
import campusfind.demo.service.DashboardService;
import campusfind.demo.service.FoundItemService;
import campusfind.demo.service.LostReportService;
import campusfind.demo.service.MatchService;
import campusfind.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private FoundItemRepository foundItemRepository;

    @Autowired
    private LostReportRepository lostReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private LostReportService lostReportService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        dataInitializer.resetDemoData();
    }

    @Test
    @DisplayName("Context loads and seeded data is present")
    void testContextAndInitialData() {
        assertTrue(userRepository.count() >= 5);
        assertTrue(foundItemRepository.count() >= 4);
        assertTrue(lostReportRepository.count() >= 3);
        assertTrue(categoryService.getAllCategories().size() >= 5);
    }

    @Test
    @DisplayName("Business Rule 1: Cannot transition directly from AVAILABLE to RETURNED (Throws BadRequestException)")
    void testBusinessRule1_DirectJumpToReturnedFails() {
        FoundItem availableItem = foundItemRepository.findByStatus(FoundStatus.AVAILABLE).get(0);
        User admin = userRepository.findByEmailIgnoreCase("admin@campus.edu").orElseThrow();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            foundItemService.updateStatus(availableItem.getId(), FoundStatus.RETURNED, admin.getId(), null);
        });

        assertTrue(ex.getMessage().contains("cannot be marked 'RETURNED' unless it is first marked 'CLAIMED'"));
    }

    @Test
    @DisplayName("Business Rule 2: Student is forbidden from updating found item status (Throws UnauthorizedException)")
    void testBusinessRule2_StudentForbiddenFromUpdatingStatus() {
        FoundItem availableItem = foundItemRepository.findByStatus(FoundStatus.AVAILABLE).get(0);
        User student = userRepository.findByEmailIgnoreCase("student@campus.edu").orElseThrow();

        assertEquals(Role.STUDENT, student.getRole());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> {
            foundItemService.updateStatus(availableItem.getId(), FoundStatus.CLAIMED, student.getId(), student.getId());
        });

        assertTrue(ex.getMessage().contains("Only an admin or the reporting staff member"));
    }

    @Test
    @DisplayName("Normal State Lifecycle: AVAILABLE -> CLAIMED -> RETURNED succeeds")
    void testNormalLifecycleFlow() {
        FoundItem item = foundItemRepository.findByStatus(FoundStatus.AVAILABLE).get(0);
        User staff = userRepository.findByEmailIgnoreCase("staff@campus.edu").orElseThrow();
        User student = userRepository.findByEmailIgnoreCase("student@campus.edu").orElseThrow();

        // Step 1: AVAILABLE -> CLAIMED
        FoundItem claimed = foundItemService.updateStatus(item.getId(), FoundStatus.CLAIMED, staff.getId(), student.getId());
        assertEquals(FoundStatus.CLAIMED, claimed.getStatus());
        assertNotNull(claimed.getClaimedBy());
        assertEquals(student.getId(), claimed.getClaimedBy().getId());

        // Step 2: CLAIMED -> RETURNED
        FoundItem returned = foundItemService.updateStatus(item.getId(), FoundStatus.RETURNED, staff.getId(), null);
        assertEquals(FoundStatus.RETURNED, returned.getStatus());
    }

    @Test
    @DisplayName("Intelligent Match Engine: correctly scores Category, Keyword, and Location matches")
    void testMatchingEngine() {
        List<ItemMatch> matches = matchService.findMatches();
        assertNotNull(matches);
        assertFalse(matches.isEmpty());

        for (ItemMatch match : matches) {
            assertTrue(match.getMatchScore() >= 50, "Matches must meet minimum category base score");
            assertNotNull(match.getMatchReason());
            assertEquals(match.getLostReport().getCategory().getId(), match.getFoundItem().getCategory().getId());
        }
    }

    @Test
    @DisplayName("Dashboard Service: returns accurate campus metrics")
    void testDashboardMetrics() {
        DashboardStats stats = dashboardService.getDashboardStats();
        assertNotNull(stats);
        assertTrue(stats.getTotalUsers() >= 5);
        assertTrue(stats.getStudentCount() >= 3);
        assertTrue(stats.getStaffCount() >= 1);
        assertTrue(stats.getPendingFoundItems() >= 1);
        assertTrue(stats.getOpenLostReports() >= 1);
    }

    @Test
    @DisplayName("Specification Search: keyword and status filters function accurately")
    void testSearchSpecifications() {
        List<FoundItem> casioItems = foundItemService.searchFoundItems(null, null, "Casio", null, null);
        assertFalse(casioItems.isEmpty());
        assertTrue(casioItems.get(0).getTitle().toLowerCase().contains("casio"));

        List<LostReport> openReports = lostReportService.searchLostReports(null, LostStatus.OPEN, null, null, null);
        assertFalse(openReports.isEmpty());
        for (LostReport r : openReports) {
            assertEquals(LostStatus.OPEN, r.getStatus());
        }
    }

    @Test
    @DisplayName("Authentication: verifies registration and login flows with password security")
    void testAuthFlows() {
        String testEmail = "newbie" + System.currentTimeMillis() + "@campus.edu";
        User registered = userService.registerUser("New Student", testEmail, "pass1234", Role.STUDENT, "555-9999");
        assertNotNull(registered.getId());
        assertEquals(testEmail, registered.getEmail());

        User loggedIn = userService.loginUser(testEmail, "pass1234");
        assertEquals(registered.getId(), loggedIn.getId());

        assertThrows(BadRequestException.class, () -> {
            userService.loginUser(testEmail, "wrongpassword");
        });
    }
}
