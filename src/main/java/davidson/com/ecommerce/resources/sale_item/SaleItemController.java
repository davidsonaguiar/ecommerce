package davidson.com.ecommerce.resources.sale_item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sale-items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    public ResponseEntity<SaleItem> getSaleItem(Long id) {
        SaleItem saleItem = saleItemService.findById(id);
        return ResponseEntity.ok(saleItem);
    }

    public ResponseEntity<List<SaleItem>> getAllSaleItems() {
        List<SaleItem> saleItems = saleItemService.findAll();
        return ResponseEntity.ok(saleItems);
    }
}
