package davidson.com.ecommerce.resources.sale;

import davidson.com.ecommerce.common.LinkBuilder;
import davidson.com.ecommerce.resources.sale.dtos.request.CreateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.request.UpdateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetSaleResponseDto;
import davidson.com.ecommerce.resources.sale_item.dto.request.UpdateSaleItemDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.cglib.core.Local;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<Sale> createSale(@RequestBody @Valid CreateSaleRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        Sale sale = saleService.create(dto, admin);

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

    @GetMapping("/reportByDate/{date}")
    public ResponseEntity<EntityModel<GetSaleResponseDto>> getSaleReportByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Sale> sales = saleService.findByDate(localDate.atStartOfDay());
        return null;
    }


    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
