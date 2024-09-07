package davidson.com.ecommerce.resources.sale;

import davidson.com.ecommerce.common.LinkBuilder;
import davidson.com.ecommerce.exceptions.UnprocessableException;
import davidson.com.ecommerce.resources.sale.dtos.request.CreateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.request.UpdateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetReportResponseDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetSaleResponseDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.EntityModel;
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
    private final LinkBuilder linkBuilder;

    public SaleController(SaleService saleService, LinkBuilder linkBuilder) {
        this.saleService = saleService;
        this.linkBuilder = linkBuilder;
    }

    @PostMapping
    @CacheEvict(value = "sales", allEntries = true)
    public ResponseEntity<Sale> createSale(@RequestBody @Valid CreateSaleRequestDto dto) {
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
    public ResponseEntity<EntityModel<GetSaleResponseDto>> getSale(@PathVariable Long id) {
        Sale sale = saleService.findById(id);
        EntityModel<GetSaleResponseDto> entityModel = EntityModel.of(GetSaleResponseDto.fromEntity(sale));
        entityModel.add(linkBuilder.linkToSales().withRel("allSales"));
        entityModel.add(linkBuilder.linkToUser(sale.getSoldTo().getId()).withRel("client"));
        sale.getSaleItems().forEach(saleItem -> {
            entityModel.add(linkBuilder.linkToProduct(saleItem.getProduct().getId()).withRel("products"));
        });
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping
    @Cacheable("sales")
    public ResponseEntity<List<EntityModel<GetSaleResponseDto>>> getAllSales() {
        List<Sale> sales = saleService.findAll();
        List<EntityModel<GetSaleResponseDto>> entityModels = sales
                .stream()
                .map((sale) -> {
                    EntityModel<GetSaleResponseDto> entityModel = EntityModel.of(GetSaleResponseDto.fromEntity(sale));
                    entityModel.add(linkBuilder.linkToSale(sale.getId()).withRel("sale"));
                    entityModel.add(linkBuilder.linkToUser(sale.getSoldTo().getId()).withRel("client"));
                    sale.getSaleItems().forEach(saleItem -> {
                        entityModel.add(linkBuilder.linkToProduct(saleItem.getProduct().getId()).withRel("products"));
                    });
                    return entityModel;
                })
                .toList();
        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/report")
    public ResponseEntity<GetReportResponseDto> getSaleReportByDate(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "month", required = false) Integer month){

        GetReportResponseDto report = null;

        if(date != null && month != null) throw new UnprocessableException("Only one parameter is allowed");

        if(date != null) {
            LocalDateTime start = LocalDate.parse(date).atStartOfDay();
            List<Sale> sales = saleService.findByDate(start, start.plusDays(1));
            if(!sales.isEmpty()) report = getReport(sales);
        }

        if(month != null) {
            LocalDateTime start = LocalDate.now()
                    .withMonth(month)
                    .withDayOfMonth(1)
                    .atStartOfDay();
            List<Sale> sales = saleService.findByDate(start, start.plusMonths(1));
            if(!sales.isEmpty()) report = getReport(sales);
        }

        if(date == null && month == null) {
            Integer dayWeek = LocalDate.now().getDayOfWeek().getValue();
            LocalDateTime start = LocalDate.now().minusDays(dayWeek - 1).atStartOfDay();
            List<Sale> sales = saleService.findByDate(start, start.plusWeeks(1));
            if(!sales.isEmpty()) report = getReport(sales);
        }

        if(report == null) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(report);
    }

    private GetReportResponseDto getReport(List<Sale> sales) {
        Integer numberSales = sales.size();
        Integer itemsSold = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        for(Sale sale : sales) {
            itemsSold += sale.getSaleItems().size();
            totalValue = totalValue.add(sale.getTotalValue());
        }
        return  new GetReportResponseDto(numberSales, itemsSold, totalValue);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "sales", allEntries = true)
    public ResponseEntity<EntityModel<GetSaleResponseDto>> updateSale(@PathVariable Long id, @RequestBody @Valid UpdateSaleRequestDto dto) {
        Sale sale = saleService.update(id, dto);
        EntityModel<GetSaleResponseDto> entityModel = EntityModel.of(GetSaleResponseDto.fromEntity(sale));
        entityModel.add(linkBuilder.linkToSales().withRel("allSales"));
        entityModel.add(linkBuilder.linkToUser(sale.getSoldTo().getId()).withRel("client"));
        sale.getSaleItems().forEach(saleItem -> {
            entityModel.add(linkBuilder.linkToProduct(saleItem.getProduct().getId()).withRel("products"));
        });
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "sales", allEntries = true)
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
