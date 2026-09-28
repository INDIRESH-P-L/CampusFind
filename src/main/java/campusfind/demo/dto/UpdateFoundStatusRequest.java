package campusfind.demo.dto;

import campusfind.demo.entity.FoundStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateFoundStatusRequest {

    @NotNull(message = "New status is required (AVAILABLE, CLAIMED, or RETURNED)")
    private FoundStatus status;

    @NotNull(message = "Current User ID is required to verify authorization")
    private Long currentUserId;

    private Long claimedByUserId; // Optional: user who claimed the item

    public UpdateFoundStatusRequest() {
    }

    public UpdateFoundStatusRequest(FoundStatus status, Long currentUserId, Long claimedByUserId) {
        this.status = status;
        this.currentUserId = currentUserId;
        this.claimedByUserId = claimedByUserId;
    }

    public FoundStatus getStatus() {
        return status;
    }

    public void setStatus(FoundStatus status) {
        this.status = status;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Long getClaimedByUserId() {
        return claimedByUserId;
    }

    public void setClaimedByUserId(Long claimedByUserId) {
        this.claimedByUserId = claimedByUserId;
    }
}
