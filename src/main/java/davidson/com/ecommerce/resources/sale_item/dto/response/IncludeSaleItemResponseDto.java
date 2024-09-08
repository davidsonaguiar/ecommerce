package davidson.com.ecommerce.resources.sale_item.dto.response;

import davidson.com.ecommerce.resources.product.ProductController;
import davidson.com.ecommerce.resources.sale_item.SaleItem;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record IncludeSaleItemResponseDto(
        Long id,
        String name,
        String brand,
        String model,
        BigDecimal price,
        Integer quantity,
        URI link
) {

    public static IncludeSaleItemResponseDto fromEntity(SaleItem saleItem) {
        if (saleItem == null) return null;
        return new IncludeSaleItemResponseDto(
                saleItem.getProduct().getId(),
                saleItem.getProduct().getName(),
                saleItem.getProduct().getBrand(),
                saleItem.getProduct().getModel(),
                saleItem.getPrice(),
                saleItem.getQuantity(),
                linkTo(methodOn(ProductController.class).getProduct(saleItem.getProduct().getId())).toUri()
        );
    }

    public static Set<IncludeSaleItemResponseDto> fromEntities(Set<SaleItem> saleItems) {
        return saleItems.stream()
                .map(IncludeSaleItemResponseDto::fromEntity)
                .collect(Collectors.toSet());
    }
}
