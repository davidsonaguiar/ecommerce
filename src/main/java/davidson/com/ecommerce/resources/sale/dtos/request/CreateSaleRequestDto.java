package davidson.com.ecommerce.resources.sale.dtos.request;

import davidson.com.ecommerce.resources.sale_item.dto.request.CreateSaleItemRequestDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateSaleRequestDto(
        @NotEmpty(message = "Items cannot be empty")
        List<CreateSaleItemRequestDto> products
) {}
