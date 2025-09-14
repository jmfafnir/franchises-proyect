package co.com.franchises.usecase.managefranchise;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.franchise.gateways.FranchiseRepository;
import co.com.franchises.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ManageFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> addFranchise(Franchise franchise){
        return franchiseRepository.addFranchise(franchise);
    }

    public Mono<Franchise> changeName(String oldName,String newName){
        return franchiseRepository.upDateNameFranchise(oldName,newName);
    }

    public Mono<Franchise> addBranchToFranchise(String franchiseName, Branch branch){
        return franchiseRepository.addBranch(franchiseName,branch);
    }

    public Mono<Map<String, Product>> getProductMostStockPerBranch(String franchiseName){
        return franchiseRepository.getFranchise(franchiseName)
                .map(this::orderProductMostStock)
                .filter(productMap -> !productMap.isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(BussinesExceptionEnum.BUSINESS_GET_PRODUCTS_MAX_STOCK)));
    }


    private Map<String,Product> orderProductMostStock(Franchise franchise){
        if (franchise == null || franchise.getBranches() == null) {
            return Collections.emptyMap();
        }

        return franchise.getBranches().stream()
                .filter(branch -> branch.getProducts() != null && !branch.getProducts().isEmpty())
                .collect(Collectors.toMap(
                        Branch::getBranchName,
                        branch -> branch.getProducts().stream()
                                .max(Comparator.comparingInt(Product::getStock))
                                .orElse(null)
                ));
    }


}
