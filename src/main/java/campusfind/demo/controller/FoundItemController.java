package campusfind.demo.controller;

import campusfind.demo.dto.CreateFoundItemRequest;
import campusfind.demo.dto.UpdateFoundStatusRequest;
import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import campusfind.demo.service.FoundItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
@CrossOrigin(origins = "*")
public class FoundItemController {

    private final FoundItemService foundItemService;

    public FoundItemController(FoundItemService foundItemService) {
        this.foundItemService = foundItemService;
    }

    @PostMapping
    public ResponseEntity<FoundItem> createFoundItem(@Valid @RequestBody CreateFoundItemRequest request) {
        FoundItem created = foundItemService.createFoundItem(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getFoundDate(),
                request.getFinderId(),
                request.getCategoryId()
        );
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoundItem>> getFoundItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) FoundStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate) {
        return ResponseEntity.ok(foundItemService.searchFoundItems(categoryId, status, keyword, startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getFoundItemById(@PathVariable Long id) {
        return ResponseEntity.ok(foundItemService.getFoundItemById(id));
    }

    @GetMapping("/finder/{finderId}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByFinder(@PathVariable Long finderId) {
        return ResponseEntity.ok(foundItemService.getFoundItemsByFinderId(finderId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FoundItem> updateFoundItemStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFoundStatusRequest request) {
        FoundItem updated = foundItemService.updateStatus(
                id,
                request.getStatus(),
                request.getCurrentUserId(),
                request.getClaimedByUserId()
        );
        return ResponseEntity.ok(updated);
    }
}
