package davidson.com.ecommerce.resources.sale;

import davidson.com.ecommerce.exceptions.ContentConflictException;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.product.ProductRepository;
import davidson.com.ecommerce.resources.sale.dtos.request.CreateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.request.UpdateSaleRequestDto;
import davidson.com.ecommerce.resources.sale_item.SaleItem;
import davidson.com.ecommerce.resources.sale_item.SaleItemRepository;
import davidson.com.ecommerce.resources.sale_item.dto.request.UpdateSaleItemDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository, SaleItemRepository saleItemRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.saleItemRepository = saleItemRepository;
    }

    @Transactional
    public Sale create(CreateSaleRequestDto dto, User soldTo) {
        Sale sale = new Sale();
        sale.setSoldTo(soldTo);
        sale.setSoldAt(LocalDateTime.now());
        Sale saleSaved = saleRepository.save(sale);

        List<SaleItem> saleItems = dto.products().stream().map(productDto -> {
            Long id = productDto.productId();
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if (!product.getActive()) throw new ContentConflictException("Product unavailable");
            if (product.getQuantity() < productDto.quantity())
                throw new ContentConflictException("Product quantity is not enough");

            product.setQuantity(product.getQuantity() - productDto.quantity());
            productRepository.save(product);

            SaleItem saleItem = new SaleItem(product, saleSaved, productDto.quantity(), product.getPrice());
            return saleItem;
        }).toList();

        List<SaleItem> saleItemsSaved = saleItemRepository.saveAll(saleItems);
        saleSaved.getSaleItems().addAll(saleItemsSaved);
        return saleSaved;
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Transactional
    public Sale update(Long id, UpdateSaleRequestDto dto) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        sale.getSaleItems().forEach(item -> {
            if (dto.products().stream().noneMatch(productDto -> productDto.productId().equals(item.getProduct().getId()))) {
                System.out.println(item);
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                item.setQuantity(0);
                productRepository.save(product);
                saleItemRepository.delete(item);
            } else {
                UpdateSaleItemDto updateSaleItemDto = dto.products().stream().
                        filter(productDto -> productDto.productId().equals(item.getProduct().getId()))
                        .findFirst().get();

                Integer difference = item.getQuantity() - updateSaleItemDto.quantity();

                if (difference == 0) return;

                item.setQuantity(updateSaleItemDto.quantity());
                saleItemRepository.save(item);

                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + difference);
                productRepository.save(product);
            }
        });


        dto.products().forEach(item -> {
            if (sale.getSaleItems().stream().noneMatch(saleItem -> saleItem.getProduct().getId().equals(item.productId()))) {
                Product product = productRepository.findById(item.productId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                if (!product.getActive()) throw new ContentConflictException("Product unavailable");
                if (product.getQuantity() < item.quantity())throw new ContentConflictException("Product quantity is not enough");

                product.setQuantity(product.getQuantity() - item.quantity());
                productRepository.save(product);

                SaleItem saleItem = new SaleItem(product, sale, item.quantity(), product.getPrice());
                sale.getSaleItems().add(saleItem);
            }
        });

        sale.getSaleItems().removeIf(item -> item.getQuantity() == 0);
        return saleRepository.save(sale);
    }

    public void delete(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        saleRepository.delete(sale);
    }
}
