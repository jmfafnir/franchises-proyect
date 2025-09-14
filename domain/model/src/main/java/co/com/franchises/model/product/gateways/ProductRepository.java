package co.com.franchises.model.product.gateways;



import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> upDateProduct(String productName, int newStock);
    Mono<Product> UpDateName(String oldName, String newName);
}
