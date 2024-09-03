package davidson.com.ecommerce.resources.sale_item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.sale.Sale;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sale_items")
public class SaleItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @EmbeddedId
    private SaleItemPk id = new SaleItemPk();

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than 0")
    private BigDecimal price;

    public SaleItem(Product product, Sale sale, Integer quantity, BigDecimal price) {
        id.setProduct(product);
        id.setSale(sale);
        this.quantity = quantity;
        this.price = price;
    }

    @JsonIgnore
    public Sale getSale() {
        return id.getSale();
    }

    public Product getProduct() {
        return id.getProduct();
    }

    @Override
    public String toString() {
        return """
                SaleItem: {
                    product=%s,
                    quantity=%d,
                    price=%s
                }
                """.formatted(id.getProduct(), quantity, price);
    }
}
