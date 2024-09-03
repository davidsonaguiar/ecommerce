package davidson.com.ecommerce.common;

import davidson.com.ecommerce.resources.product.ProductController;
import davidson.com.ecommerce.resources.sale.SaleController;
import davidson.com.ecommerce.resources.sale_item.SaleItemController;
import davidson.com.ecommerce.resources.user.UserController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class LinkBuilder {
    public WebMvcLinkBuilder linkToProducts() {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getAllProducts());
    }

    public WebMvcLinkBuilder linkToProduct(Long id) {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getProduct(id));
    }

    public WebMvcLinkBuilder linkToSale(Long id) {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SaleController.class).getSale(id));
    }

    public WebMvcLinkBuilder linkToSales() {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SaleController.class).getAllSales());
    }

    public WebMvcLinkBuilder linkToUser(Long id) {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id));
    }
}
