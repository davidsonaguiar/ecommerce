package davidson.com.ecommerce.resources.sale;

import davidson.com.ecommerce.exceptions.UnprocessableException;
import davidson.com.ecommerce.resources.sale.dtos.request.CreateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.request.UpdateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetAllSalesResponseDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetReportResponseDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetSaleResponseDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @CacheEvict(value = "sales", allEntries = true)
    public ResponseEntity<Void> createSale(@RequestBody @Valid CreateSaleRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Sale sale = saleService.create(dto, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(sale.getId());

        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetSaleResponseDto> getSale(@PathVariable Long id) {
        Sale sale = saleService.findById(id);
        return ResponseEntity.ok(GetSaleResponseDto.fromEntity(sale));
    }


    @GetMapping
    @Cacheable("sales")
    public ResponseEntity<List<GetAllSalesResponseDto>> getAllSales() {
        List<Sale> sales = saleService.findAll();
        return ResponseEntity.ok(GetAllSalesResponseDto.fromEntities(sales));
    }


    @GetMapping("/report")
    public ResponseEntity<GetReportResponseDto> getSaleReportByDate(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "month", required = false) Integer month) {

        GetReportResponseDto report = null;

        if (date != null && month != null) throw new UnprocessableException("Only one parameter is allowed");

        if (date != null) {
            LocalDateTime start = LocalDate.parse(date).atStartOfDay();
            List<Sale> sales = saleService.findByDate(start, start.plusDays(1));
            if (!sales.isEmpty()) report = getReport(sales);
        }

        if (month != null) {
            LocalDateTime start = LocalDate.now()
                    .withMonth(month)
                    .withDayOfMonth(1)
                    .atStartOfDay();
            List<Sale> sales = saleService.findByDate(start, start.plusMonths(1));
            if (!sales.isEmpty()) report = getReport(sales);
        }

        if (date == null && month == null) {
            Integer dayWeek = LocalDate.now().getDayOfWeek().getValue();
            LocalDateTime start = LocalDate.now().minusDays(dayWeek - 1).atStartOfDay();
            List<Sale> sales = saleService.findByDate(start, start.plusWeeks(1));
            if (!sales.isEmpty()) report = getReport(sales);
        }

        if (report == null) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(report);
    }


    private GetReportResponseDto getReport(List<Sale> sales) {
        Integer numberSales = sales.size();
        Integer itemsSold = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        for (Sale sale : sales) {
            itemsSold += sale.getSaleItems().size();
            totalValue = totalValue.add(sale.getTotalValue());
        }
        return new GetReportResponseDto(numberSales, itemsSold, totalValue);
    }


    @PutMapping("/{id}")
    @CacheEvict(value = "sales", allEntries = true)
    public ResponseEntity<GetSaleResponseDto> updateSale(@PathVariable Long id, @RequestBody @Valid UpdateSaleRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Sale sale = saleService.update(id, dto, user);
        return ResponseEntity.ok(GetSaleResponseDto.fromEntity(sale));
    }


    @DeleteMapping("/{id}")
    @CacheEvict(value = "sales", allEntries = true)
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
