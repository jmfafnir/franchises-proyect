package co.com.franchises.model.branch.gateways;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface BranchRepository {


    Mono<Branch> upDateNameBranch(String oldName,String newName);
    Mono<Product> addProduct(String branchName,Product product);
    Mono<Boolean> deleteProduct(String branchName,String productName);




}
