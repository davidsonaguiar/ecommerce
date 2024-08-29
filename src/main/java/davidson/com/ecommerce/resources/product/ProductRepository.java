package davidson.com.ecommerce.resources.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional findByNameAndBrandAndModel(String name, String brand, String model);
}
