package co.com.franchises.usecase.managebranch;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchRepository;
import co.com.franchises.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class ManageBranchUseCase {

    private final  BranchRepository branchRepository;

    public Mono<Product> addProductToBranch(String branchName, Product product){
        return branchRepository.addProduct(branchName,product);
    }

    public Mono<Boolean> deleteProductToBranch(String branchName, String productName){
        return branchRepository.deleteProduct(branchName,branchName);
    }

    public Mono<Branch> changeName(String oldName, String newName){
        return branchRepository.upDateNameBranch(oldName,newName);
    }



}
