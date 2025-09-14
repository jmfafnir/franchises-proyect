package co.com.franchises.r2dbc.franchiseRepositoryAdapter;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.franchise.gateways.FranchiseRepository;
import co.com.franchises.model.product.Product;
import co.com.franchises.r2dbc.branchRepositoryAdapter.BranchDataRepository;
import co.com.franchises.r2dbc.dto.BranchData;
import co.com.franchises.r2dbc.dto.FranchiseData;
import co.com.franchises.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchises.r2dbc.productRepositoryAdapter.ProductDataRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Repository
public class FranchiseRepositoryAdapter extends ReactiveAdapterOperations<
    Franchise, FranchiseData, String, FranchiseDataRepository
> implements FranchiseRepository {

    private final BranchDataRepository branchRepository;
    private final ProductDataRepository productRepository;

    public FranchiseRepositoryAdapter(FranchiseDataRepository repository,
                                      BranchDataRepository branchRepository,
                                      ProductDataRepository productRepository,
                                      ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, Franchise.class));
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Franchise> getFranchise(String franchiseName) {
        return repository.findById(franchiseName)
                .flatMap(franchiseData ->
                        branchRepository.findAllByFranchiseId(franchiseData.getId())
                                .flatMap(branchData ->
                                        productRepository.findAllByBranchId(branchData.getId())
                                                .map(productData -> mapper.map(productData, Product.class))
                                                .collectList()
                                                .map(products -> Branch.builder()
                                                        .branchName(branchData.getId())
                                                        .products(products)
                                                        .build())
                                )
                                .collectList()
                                .map(branches -> Franchise.builder()
                                        .franchiseName(franchiseData.getId())
                                        .branches(branches)
                                        .build()
                                )
                );
    }

    @Override
    public Mono<Franchise> addFranchise(Franchise franchise) {
        FranchiseData franchiseData = FranchiseData.builder()
                .id(franchise.getFranchiseName())
                .build();

        return repository.save(franchiseData)
                .map(this::toEntity);

    }

    @Override
    public Mono<Franchise> upDateNameFranchise(String oldName, String newName) {
        return repository.findById(oldName)
                .switchIfEmpty(Mono.error(new BusinessException(BussinesExceptionEnum.BUSINESS_FRANCHISE_NOT_FOUND)))
                .map(franchiseData -> franchiseData.toBuilder()
                        .id(newName)
                        .build())
                .flatMap(repository::save)
                .map(this::toEntity);
    }

    @Override
    public Mono<Franchise> addBranch(String franchiseName, Branch branch) {
        return repository.findById(franchiseName)
                .switchIfEmpty(Mono.error(new BusinessException(BussinesExceptionEnum.BUSINESS_FRANCHISE_NOT_FOUND)))
                .flatMap(franchiseData ->
                        branchRepository.findById(branch.getBranchName())
                                .flatMap(existing -> Mono.<Franchise>error(
                                        new BusinessException(BussinesExceptionEnum.BRANCH_ALREADY_EXISTS)))
                                .switchIfEmpty(
                                        branchRepository.save(BranchData.builder()
                                                        .id(branch.getBranchName())
                                                        .franchiseId(franchiseName)
                                                        .build())
                                                .then(branchRepository.findAllByFranchiseId(franchiseName)
                                                        .map(bd -> Branch.builder()
                                                                .branchName(bd.getId())
                                                                .products(Collections.emptyList())
                                                                .build())
                                                        .collectList()
                                                        .map(branches -> Franchise.builder()
                                                                .franchiseName(franchiseData.getId())
                                                                .branches(branches)
                                                                .build())
                                                )
                                )
                );
    }
}
