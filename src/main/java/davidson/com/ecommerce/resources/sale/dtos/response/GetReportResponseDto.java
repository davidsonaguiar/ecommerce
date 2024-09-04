package davidson.com.ecommerce.resources.sale.dtos.response;

import java.math.BigDecimal;

public record GetReportResponseDto(
        Integer quantitySales,
        Integer quantityItemsSold,
        BigDecimal totalValue
) {}
