package campusfind.demo.repository;

import campusfind.demo.entity.LostReport;
import campusfind.demo.entity.LostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostReportRepository extends JpaRepository<LostReport, Long>, JpaSpecificationExecutor<LostReport> {

    List<LostReport> findByStatus(LostStatus status);

    List<LostReport> findByUserId(Long userId);

    List<LostReport> findByCategoryId(Long categoryId);

    List<LostReport> findByCategoryIdAndStatus(Long categoryId, LostStatus status);

    long countByStatus(LostStatus status);
}
