package davidson.com.ecommerce.resources.sale;

import davidson.com.ecommerce.resources.sale.dtos.request.CreateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.request.UpdateSaleRequestDto;
import davidson.com.ecommerce.resources.sale.dtos.response.GetSaleResponseDto;
import davidson.com.ecommerce.resources.sale_item.dto.request.UpdateSaleItemDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody @Valid CreateSaleRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        Sale sale = saleService.create(dto, admin);

        System.out.println("Sale created: " + sale);

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
        WebMvcLinkBuilder linkToSales = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllSales());
        entityModel.add(linkToSales.withRel("allSales"));
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<GetSaleResponseDto>>> getAllSales() {
        List<Sale> sales = saleService.findAll();
        List<EntityModel<GetSaleResponseDto>> entityModels = sales
                .stream()
                .map((sale) -> {
                    EntityModel<GetSaleResponseDto> entityModel = EntityModel.of(GetSaleResponseDto.fromEntity(sale));
                    WebMvcLinkBuilder linkToSale = WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getSale(sale.getId()));
                    entityModel.add(linkToSale.withRel("sale"));
                    return entityModel;
                })
                .toList();
        return ResponseEntity.ok(entityModels);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GetSaleResponseDto>> updateSale(@PathVariable Long id, @RequestBody @Valid UpdateSaleRequestDto dto) {
        Sale sale = saleService.update(id, dto);
        EntityModel<GetSaleResponseDto> entityModel = EntityModel.of(GetSaleResponseDto.fromEntity(sale));
        WebMvcLinkBuilder linkToSales = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllSales());
        entityModel.add(linkToSales.withRel("allSales"));
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
