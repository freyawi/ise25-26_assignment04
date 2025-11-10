package de.seuhd.campuscoffee.data.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import de.seuhd.campuscoffee.domain.model.SaleStatus;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<SaleEntity> findByCashierId(Long cashierId);
    List<SaleEntity> findByStatus(SaleStatus status);
    List<SaleEntity> findByTimestampBetweenAndStatus(LocalDateTime start, LocalDateTime end, SaleStatus status);
}