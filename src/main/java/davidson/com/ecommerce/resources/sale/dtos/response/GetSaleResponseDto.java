package davidson.com.ecommerce.resources.sale.dtos.response;

import davidson.com.ecommerce.resources.sale.Sale;
import davidson.com.ecommerce.resources.sale_item.dto.response.IncludeSaleItemResponseDto;
import davidson.com.ecommerce.resources.user.dtos.response.IncludeUserResponseDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GetSaleResponseDto(
        Long id,
        String soldAt,
        IncludeUserResponseDto soldTo,
        Set<IncludeSaleItemResponseDto> saleItems
) {
    public static GetSaleResponseDto fromEntity(Sale sale) {
        return new GetSaleResponseDto(
                sale.getId(),
                sale.getSoldAt().toString(),
                IncludeUserResponseDto.fromEntity(sale.getSoldTo()),
                IncludeSaleItemResponseDto.fromEntities(sale.getSaleItems())
        );
    }
}
