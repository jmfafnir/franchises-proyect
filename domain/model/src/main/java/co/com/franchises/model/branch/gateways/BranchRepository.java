package co.com.franchises.model.branch.gateways;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> getBranch(String branchName);
    Mono<Branch> upDateBranch(Branch branch);
    Mono<Boolean> deleteBranch(String branchName);
    Mono<Product> getProduct(String productName);
    Mono<Boolean> deleteProduct(String productName);



}
