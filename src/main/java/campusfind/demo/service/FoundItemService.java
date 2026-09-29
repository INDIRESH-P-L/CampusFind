package campusfind.demo.service;

import campusfind.demo.entity.Category;
import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import campusfind.demo.entity.Role;
import campusfind.demo.entity.User;
import campusfind.demo.exception.BadRequestException;
import campusfind.demo.exception.ResourceNotFoundException;
import campusfind.demo.exception.UnauthorizedException;
import campusfind.demo.repository.CategoryRepository;
import campusfind.demo.repository.FoundItemRepository;
import campusfind.demo.repository.UserRepository;
import campusfind.demo.specification.FoundItemSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FoundItemService {

    private final FoundItemRepository foundItemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public FoundItemService(FoundItemRepository foundItemRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository) {
        this.foundItemRepository = foundItemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public FoundItem createFoundItem(String title, String description, String location,
                                     LocalDate foundDate, Long finderId, Long categoryId) {
        if (foundDate != null && foundDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Found date cannot be in the future: " + foundDate);
        }

        User finder = userRepository.findById(finderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finderId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        FoundItem item = new FoundItem();
        item.setTitle(title != null ? title.trim() : "");
        item.setDescription(description != null ? description.trim() : "");
        item.setLocation(location != null ? location.trim() : "");
        item.setFoundDate(foundDate != null ? foundDate : LocalDate.now());
        item.setFinder(finder);
        item.setCategory(category);
        item.setStatus(FoundStatus.AVAILABLE);

        return foundItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FoundItem getFoundItemById(Long id) {
        return foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoundItem not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FoundItem> getFoundItemsByFinderId(Long finderId) {
        return foundItemRepository.findByFinderId(finderId);
    }

    @Transactional(readOnly = true)
    public List<FoundItem> getAvailableFoundItems() {
        return foundItemRepository.findByStatus(FoundStatus.AVAILABLE);
    }

    @Transactional(readOnly = true)
    public List<FoundItem> searchFoundItems(Long categoryId, FoundStatus status, String keyword, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("startDate cannot be after endDate.");
        }
        return foundItemRepository.findAll(FoundItemSpecification.filter(categoryId, status, keyword, startDate, endDate));
    }

    /**
     * Enforces the required business rules:
     * 1. Only an ADMIN or the reporting STAFF member who logged the item can change a found item's status.
     *    Students are rejected with 403 Forbidden.
     * 2. A found item cannot be marked 'RETURNED' unless it is first marked 'CLAIMED'.
     *    Direct jump from AVAILABLE to RETURNED is rejected with 400 Bad Request.
     */
    public FoundItem updateStatus(Long foundItemId, FoundStatus newStatus, Long currentUserId, Long claimedByUserId) {
        FoundItem foundItem = getFoundItemById(foundItemId);

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User performing action not found with id: " + currentUserId));

        // Business Rule 1: Authorization Check
        // Only ADMIN or campus STAFF members can change status. Students cannot!
        boolean isStaffOrAdmin = currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.STAFF;
        if (!isStaffOrAdmin) {
            throw new UnauthorizedException("Forbidden: Only an admin or the reporting staff member can change this found item's status.");
        }

        // Business Rule 2: State Workflow Validation (checked BEFORE saving)
        if (newStatus == FoundStatus.RETURNED && foundItem.getStatus() != FoundStatus.CLAIMED) {
            throw new BadRequestException("Invalid Status Transition: A found item cannot be marked 'RETURNED' unless it is first marked 'CLAIMED'. Current status is: " + foundItem.getStatus());
        }

        // Handle Claim and Reset Transitions
        if (newStatus == FoundStatus.AVAILABLE) {
            foundItem.setClaimedBy(null);
        } else if (claimedByUserId != null) {
            User claimant = userRepository.findById(claimedByUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("Claimant user not found with id: " + claimedByUserId));
            foundItem.setClaimedBy(claimant);
        }

        foundItem.setStatus(newStatus);
        return foundItemRepository.save(foundItem);
    }
}
