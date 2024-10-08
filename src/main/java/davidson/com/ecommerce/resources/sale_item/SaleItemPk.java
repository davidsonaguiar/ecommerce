package davidson.com.ecommerce.resources.sale_item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.sale.Sale;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SaleItemPk implements Serializable {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Sale_id")
    private Sale sale;
}
