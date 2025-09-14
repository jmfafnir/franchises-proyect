package co.com.franchises.r2dbc.productRepositoryAdapter;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.gateways.ProductRepository;
import co.com.franchises.r2dbc.dto.ProductData;
import co.com.franchises.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryAdapter extends ReactiveAdapterOperations<
        Product, ProductData, String, ProductDataRepository>
implements ProductRepository {


    public ProductRepositoryAdapter(ProductDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d,Product.class));
    }

    @Override
    public Mono<Product> upDateProduct(String productName, int newStock) {
        return repository.findById(productName)
                .switchIfEmpty(Mono.error(
                        new BusinessException(BussinesExceptionEnum.BUSINESS_PRODUCT_NOT_FOUND)))
                .map(productData -> productData.toBuilder()
                        .stock(newStock)
                        .build())
                .flatMap(repository::save)
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> UpDateName(String oldName, String newName) {
        return repository.findById(oldName)
                .switchIfEmpty(Mono.error(
                        new BusinessException(BussinesExceptionEnum.BUSINESS_PRODUCT_NOT_FOUND)))
                .map(productData -> productData.toBuilder()
                        .id(newName)
                        .build())
                .flatMap(repository::save)
                .map(this::toEntity);
    }
}
