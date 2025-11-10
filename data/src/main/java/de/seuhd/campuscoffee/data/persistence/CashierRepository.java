package de.seuhd.campuscoffee.data.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CashierRepository extends JpaRepository<CashierEntity, Long> {
    Optional<CashierEntity> findByUsername(String username);
    List<CashierEntity> findByActiveTrue();
}