package davidson.com.ecommerce.resources.sale.dtos.request;

import davidson.com.ecommerce.resources.sale_item.dto.request.UpdateSaleItemDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateSaleRequestDto(
        @NotEmpty(message = "Sale items is required")
        List<UpdateSaleItemDto> products
) {}
