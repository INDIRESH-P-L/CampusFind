package campusfind.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "CampusFind — Lost and Found Item Tracker for Campus");
        response.put("status", "UP & RUNNING");
        response.put("version", "1.0.0");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("POST /api/auth/register", "Register a student, staff, or admin");
        endpoints.put("POST /api/auth/login", "Login user with email and password");
        endpoints.put("GET /api/categories", "List all item categories");
        endpoints.put("POST /api/categories", "Create a new category");
        endpoints.put("GET /api/lost-reports", "List & search lost reports (supports ?categoryId=&status=&keyword=&startDate=&endDate=)");
        endpoints.put("POST /api/lost-reports", "Create a new lost item report");
        endpoints.put("GET /api/found-items", "List & search found items (supports ?categoryId=&status=&keyword=&startDate=&endDate=)");
        endpoints.put("POST /api/found-items", "Log a new found item (Staff)");
        endpoints.put("PUT /api/found-items/{id}/status", "Update found item status (AVAILABLE -> CLAIMED -> RETURNED)");
        endpoints.put("GET /api/matches", "List possible matches between lost and found items");
        endpoints.put("GET /api/admin/dashboard", "Get admin dashboard metrics and statistics");

        response.put("available_endpoints", endpoints);
        return ResponseEntity.ok(response);
    }
}
