package co.com.franchises.usecase.managebranch;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchRepository;
import co.com.franchises.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageBranchUseCaseTest {
    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private ManageBranchUseCase manageBranchUseCase;

    @BeforeEach
    void setup() {

    }

    @Test
    void testAddProductToBranch_success() {
        Product product = Product.builder().productName("Leche").stock(10).build();
        String branchName = "Sucursal Centro";

        when(branchRepository.addProduct(branchName, product))
                .thenReturn(Mono.just(product));

        StepVerifier.create(manageBranchUseCase.addProductToBranch(branchName, product))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void testDeleteProductToBranch_success() {
        String branchName = "Sucursal Centro";
        String productName = "Leche";

        when(branchRepository.deleteProduct(branchName, productName))
                .thenReturn(Mono.just(true));

        StepVerifier.create(manageBranchUseCase.deleteProductToBranch(branchName, productName))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testChangeName_success() {
        String oldName = "Sucursal Vieja";
        String newName = "Sucursal Nueva";

        Branch updatedBranch = Branch.builder()
                .branchName(newName)
                .products(null)
                .build();

        when(branchRepository.upDateNameBranch(oldName, newName))
                .thenReturn(Mono.just(updatedBranch));

        StepVerifier.create(manageBranchUseCase.changeName(oldName, newName))
                .expectNextMatches(branch -> branch.getBranchName().equals("Sucursal Nueva"))
                .verifyComplete();
    }


}
