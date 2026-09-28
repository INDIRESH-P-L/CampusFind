package campusfind.demo.repository;

import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long>, JpaSpecificationExecutor<FoundItem> {

    List<FoundItem> findByStatus(FoundStatus status);

    List<FoundItem> findByFinderId(Long finderId);

    List<FoundItem> findByCategoryId(Long categoryId);

    List<FoundItem> findByCategoryIdAndStatus(Long categoryId, FoundStatus status);

    long countByStatus(FoundStatus status);
}
