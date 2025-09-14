package co.com.franchises.r2dbc.branchRepositoryAdapter;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchRepository;
import co.com.franchises.model.product.Product;
import co.com.franchises.r2dbc.dto.BranchData;
import co.com.franchises.r2dbc.dto.ProductData;
import co.com.franchises.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchises.r2dbc.productRepositoryAdapter.ProductDataRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BranchRepositoryAdapter extends ReactiveAdapterOperations<
        Branch, BranchData, String,BranchDataRepository>
        implements BranchRepository {

    private final ProductDataRepository productDataRepository;

    public BranchRepositoryAdapter(BranchDataRepository repository,
            ProductDataRepository productDataRepository, ObjectMapper mapper){
        super(repository, mapper, d -> mapper.map(d,Branch.class));
        this.productDataRepository = productDataRepository;
    }

    @Override
    public Mono<Branch> upDateNameBranch(String oldName, String newName) {
        return repository.findById(oldName)
                .switchIfEmpty(Mono.error(new BusinessException(BussinesExceptionEnum.BUSINESS_BRANCH_NOT_FOUND)))
                .map(branchData -> branchData.toBuilder()
                        .id(newName)
                        .build())
                .flatMap(repository::save)
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> addProduct(String branchName, Product product) {
        return repository.findById(branchName)
                .switchIfEmpty(Mono.error(new BusinessException(BussinesExceptionEnum.BUSINESS_BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        productDataRepository.findById(product.getProductName())
                        .flatMap(productData -> Mono.<Product>error(
                                new BusinessException(BussinesExceptionEnum.PRODUCT_ALREADY_EXISTS)))
                        .switchIfEmpty(productDataRepository.save(ProductData.builder()
                                        .id(product.getProductName())
                                        .stock(product.getStock())
                                        .branchId(branchName)
                                        .build())
                                .map(productData -> Product.builder()
                                        .productName(productData.getId())
                                        .stock(productData.getStock())
                                        .build())
                        )
                );
    }

    @Override
    public Mono<Boolean> deleteProduct(String branchName, String productName) {
        return repository.findById(branchName)
                .switchIfEmpty(Mono.error(
                        new BusinessException(BussinesExceptionEnum.BUSINESS_BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        productDataRepository.findById(productName)
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(BussinesExceptionEnum.BUSINESS_PRODUCT_NOT_FOUND)))
                                .flatMap(productData ->
                                        productDataRepository.delete(productData)
                                                .thenReturn(true)
                                )
                );
    }
}
