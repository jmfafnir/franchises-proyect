package co.com.franchises.model.branch.gateways;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> getBranch(String branchName);
    Mono<Branch> upDateNameBranch(String oldName,String newName);
    Mono<Boolean> deleteBranch(String branchName);
    Mono<Product> addProduct(String branchName,Product product);
    Mono<Boolean> deleteProduct(String branchName,String productName);




}
