package davidson.com.ecommerce.resources.sale.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import davidson.com.ecommerce.resources.sale.Sale;
import davidson.com.ecommerce.resources.sale.SaleController;
import davidson.com.ecommerce.resources.sale_item.dto.response.IncludeSaleItemResponseDto;
import davidson.com.ecommerce.resources.user.dtos.response.IncludeUserResponseDto;
import org.springframework.hateoas.Links;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public record GetSaleResponseDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss'Z'", timezone = "UTC")
        LocalDateTime soldAt,
        IncludeUserResponseDto soldTo,
        Set<IncludeSaleItemResponseDto> saleItems,
        URI link
) {
    public static GetSaleResponseDto fromEntity(Sale sale) {
        return new GetSaleResponseDto(
                sale.getId(),
                sale.getSoldAt(),
                IncludeUserResponseDto.fromEntity(sale.getSoldTo()),
                IncludeSaleItemResponseDto.fromEntities(sale.getSaleItems()),
                linkTo(methodOn(SaleController.class).getSale(sale.getId())).toUri()
        );
    }
}
