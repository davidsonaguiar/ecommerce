package davidson.com.ecommerce.resources.sale_item.dto.response;

import davidson.com.ecommerce.resources.sale_item.SaleItem;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record IncludeSaleItemResponseDto(
        Long id,
        String name,
        String brand,
        String model,
        BigDecimal price,
        Integer quantity
) {

    public static IncludeSaleItemResponseDto fromEntity(SaleItem saleItem) {
        if (saleItem == null) return null;
        return new IncludeSaleItemResponseDto(
                saleItem.getProduct().getId(),
                saleItem.getProduct().getName(),
                saleItem.getProduct().getBrand(),
                saleItem.getProduct().getModel(),
                saleItem.getPrice(),
                saleItem.getQuantity()
        );
    }

    public static Set<IncludeSaleItemResponseDto> fromEntities(Set<SaleItem> saleItems) {
        return saleItems.stream()
                .map(IncludeSaleItemResponseDto::fromEntity)
                .collect(Collectors.toSet());
    }
}
