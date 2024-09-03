package davidson.com.ecommerce.resources.sale_item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleItemService {
    private final SaleItemRepository saleItemRepository;

    public SaleItemService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public SaleItem findById(Long id) {
        return saleItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale item not found"));
    }

    public List<SaleItem> findAll() {
        return saleItemRepository.findAll();
    }
}
