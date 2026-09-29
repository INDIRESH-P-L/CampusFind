package campusfind.demo.controller;

import campusfind.demo.config.DataInitializer;
import campusfind.demo.dto.DashboardStats;
import campusfind.demo.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final DataInitializer dataInitializer;

    public DashboardController(DashboardService dashboardService, DataInitializer dataInitializer) {
        this.dashboardService = dashboardService;
        this.dataInitializer = dataInitializer;
    }

    @GetMapping({"/api/admin/dashboard", "/api/dashboard/stats"})
    public ResponseEntity<DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @PostMapping({"/api/admin/reset-demo-data", "/api/auth/reset-demo"})
    public ResponseEntity<Map<String, String>> resetDemoData() {
        dataInitializer.resetDemoData();
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Demo database successfully reset to pristine initial state with seeded records."
        ));
    }
}
