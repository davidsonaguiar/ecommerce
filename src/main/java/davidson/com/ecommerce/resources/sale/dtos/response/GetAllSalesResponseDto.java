package davidson.com.ecommerce.resources.sale.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import davidson.com.ecommerce.resources.product.dto.response.GetAllProductsResponseDto;
import davidson.com.ecommerce.resources.sale.Sale;
import davidson.com.ecommerce.resources.sale.SaleController;
import org.springframework.hateoas.Links;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetAllSalesResponseDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss'Z'", timezone = "UTC")
        LocalDateTime soldAt,
        BigDecimal total,
        Integer quantityItems,
        URI link
) {
    public static GetAllSalesResponseDto fromEntity(Sale sale) {
        return new GetAllSalesResponseDto(
                sale.getId(),
                sale.getSoldAt(),
                sale.getTotalValue(),
                sale.getSaleItems().size(),
                linkTo(methodOn(SaleController.class).getSale(sale.getId())).toUri()
        );
    }

    public static List<GetAllSalesResponseDto> fromEntities(List<Sale> sales) {
        return sales.stream().map(GetAllSalesResponseDto::fromEntity).toList();
    }
}
