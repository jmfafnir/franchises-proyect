package co.com.franchises.model.product.gateways;



import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {


    Mono<Product> getProduct(String productName);
    Mono<Product> upDateProduct(Product product);
    Mono<Boolean> deleteProduct(String productName);
    Mono<Product> getStock(String productName);
}
