package davidson.com.ecommerce.resources.sale;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySoldAtBetween(LocalDateTime start, LocalDateTime end);
}
