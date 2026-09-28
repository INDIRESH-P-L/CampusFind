package campusfind.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateFoundItemRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private LocalDate foundDate;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Finder (Staff) ID is required")
    private Long finderId;

    public CreateFoundItemRequest() {
    }

    public CreateFoundItemRequest(String title, String description, String location, LocalDate foundDate, Long categoryId, Long finderId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.foundDate = foundDate;
        this.categoryId = categoryId;
        this.finderId = finderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(LocalDate foundDate) {
        this.foundDate = foundDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getFinderId() {
        return finderId;
    }

    public void setFinderId(Long finderId) {
        this.finderId = finderId;
    }
}
