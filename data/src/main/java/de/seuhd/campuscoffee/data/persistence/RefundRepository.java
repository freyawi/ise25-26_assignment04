package de.seuhd.campuscoffee.data.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
    List<RefundEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<RefundEntity> findByCashierId(Long cashierId);
    List<RefundEntity> findByOriginalSaleId(Long saleId);
}