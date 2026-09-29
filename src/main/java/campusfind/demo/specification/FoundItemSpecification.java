package campusfind.demo.specification;

import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoundItemSpecification {

    public static Specification<FoundItem> filter(Long categoryId,
                                                 FoundStatus status,
                                                 String keyword,
                                                 LocalDate startDate,
                                                 LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter by Category
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            // 2. Filter by Status (AVAILABLE / CLAIMED / RETURNED)
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // 3. Keyword Search across title, description, and location
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), pattern);
                Predicate descMatch = cb.like(cb.lower(root.get("description")), pattern);
                Predicate locationMatch = cb.like(cb.lower(root.get("location")), pattern);
                predicates.add(cb.or(titleMatch, descMatch, locationMatch));
            }

            // 4. Date Range Filters (From startDate to endDate)
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("foundDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("foundDate"), endDate));
            }

            // Default order by foundDate descending (only for entity queries, not count queries)
            if (query != null && (query.getResultType() == null || !Number.class.isAssignableFrom(query.getResultType()))) {
                query.orderBy(cb.desc(root.get("foundDate")), cb.desc(root.get("id")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
