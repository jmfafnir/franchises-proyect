package co.com.franchises.usecase.manageproduct;

import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ManageProductUseCase {

    private final ProductRepository productRepository;

    public Mono<Product> upDateNameProduct(String oldName, String newName){
        return productRepository.UpDateName(oldName,newName);
    }

    public Mono<Product> upDateStock(String ProductName, int newStock){
        return productRepository.upDateProduct(ProductName,newStock);
    }

}
