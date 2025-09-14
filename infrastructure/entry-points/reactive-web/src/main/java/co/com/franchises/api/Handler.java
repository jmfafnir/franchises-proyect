package co.com.franchises.api;

import co.com.franchises.api.dto.*;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.product.Product;
import co.com.franchises.usecase.managebranch.ManageBranchUseCase;
import co.com.franchises.usecase.managefranchise.ManageFranchiseUseCase;
import co.com.franchises.usecase.manageproduct.ManageProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final ManageFranchiseUseCase franchiseUseCase;
    private final ManageBranchUseCase branchUseCase;
    private final ManageProductUseCase productUseCase;

    public Mono<ServerResponse> changeFranchiseName(ServerRequest request) {
        return request.bodyToMono(ChangeFranchiseNameRequest.class)
                .flatMap(dto -> franchiseUseCase.changeName(dto.getOldName(), dto.getNewName()))
                .flatMap(f -> ServerResponse.ok().bodyValue(f));
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        return request.bodyToMono(AddBranchRequest.class)
                .flatMap(dto -> franchiseUseCase.addBranchToFranchise(
                        dto.getFranchiseName(),
                        Branch.builder()
                                .branchName(dto.getBranchName())
                                .build()
                ))
                .flatMap(f -> ServerResponse.ok().bodyValue(f));
    }

    public Mono<ServerResponse> addFranchise(ServerRequest request) {
        return request.bodyToMono(AddFranchiseRequest.class)
                .flatMap(dto ->
                        franchiseUseCase.addFranchise(Franchise.builder()
                                .franchiseName(dto.getFranchiseName()).
                                build()))
                .flatMap(f -> ServerResponse.ok().bodyValue(f));
    }

    public Mono<ServerResponse> getInventory(ServerRequest request) {
        return request.bodyToMono(GetInventoryRequest.class)
                .flatMap(dto -> franchiseUseCase
                        .getProductMostStockPerBranch(dto.getFranchiseName()))
                .flatMap(f -> ServerResponse.ok().bodyValue(f));
    }

    public Mono<ServerResponse> changeBranchName(ServerRequest request) {
        return request.bodyToMono(ChangeBranchNameRequest.class)
                .flatMap(dto -> branchUseCase
                        .changeName(dto.getOldName(), dto.getNewName()))
                .flatMap(b -> ServerResponse.ok().bodyValue(b));
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        return request.bodyToMono(AddProductRequest.class)
                .flatMap(dto -> branchUseCase.addProductToBranch(dto.getBranchName(),
                                Product.builder()
                                        .productName(dto.getProductName())
                                        .stock(dto.getStock())
                                        .build()))
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        return request.bodyToMono(DeleteProductRequest.class)
                .flatMap(dto -> branchUseCase
                        .deleteProductToBranch(dto.getBranchName(), dto.getProductName()))
                .flatMap(result -> ServerResponse.ok().bodyValue(result));
    }

    public Mono<ServerResponse> changeProductStock(ServerRequest request) {
        return request.bodyToMono(ChangeProductStockRequest.class)
                .flatMap(dto -> productUseCase
                        .upDateStock(dto.getProductName(), dto.getNewStock()))
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    public Mono<ServerResponse> changeProductName(ServerRequest request) {
        return request.bodyToMono(ChangeProductNameRequest.class)
                .flatMap(dto -> productUseCase
                        .upDateNameProduct(dto.getOldName(), dto.getNewName()))
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }


}
