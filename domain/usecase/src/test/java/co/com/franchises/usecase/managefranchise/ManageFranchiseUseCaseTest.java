package co.com.franchises.usecase.managefranchise;

import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.franchise.gateways.FranchiseRepository;
import co.com.franchises.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private ManageFranchiseUseCase manageFranchiseUseCase;

    @BeforeEach
    void setup() {

    }

    @Test
    void testAddFranchise_success() {
        Franchise franchise = Franchise.builder()
                .franchiseName("Supermercado XYZ")
                .branches(List.of())
                .build();

        when(franchiseRepository.addFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(manageFranchiseUseCase.addFranchise(franchise))
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void testChangeName_success() {
        Franchise updated = Franchise.builder()
                .franchiseName("Nuevo Nombre")
                .branches(List.of())
                .build();

        when(franchiseRepository.upDateNameFranchise("Viejo Nombre", "Nuevo Nombre"))
                .thenReturn(Mono.just(updated));

        StepVerifier.create(manageFranchiseUseCase.changeName("Viejo Nombre", "Nuevo Nombre"))
                .expectNextMatches(f -> f.getFranchiseName().equals("Nuevo Nombre"))
                .verifyComplete();
    }

    @Test
    void testAddBranchToFranchise_success() {
        Branch branch = Branch.builder()
                .branchName("Sucursal Centro")
                .products(List.of())
                .build();

        Franchise franchise = Franchise.builder()
                .franchiseName("Supermercado XYZ")
                .branches(List.of(branch))
                .build();

        when(franchiseRepository.addBranch("Supermercado XYZ", branch))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(manageFranchiseUseCase.addBranchToFranchise("Supermercado XYZ", branch))
                .expectNextMatches(f -> f.getBranches().size() == 1 &&
                        f.getBranches().get(0).getBranchName().equals("Sucursal Centro"))
                .verifyComplete();
    }

    @Test
    void testGetProductMostStockPerBranch_success() {
        Product p1 = Product.builder().productName("Leche").stock(10).build();
        Product p2 = Product.builder().productName("Pan").stock(20).build();
        Branch b1 = Branch.builder().branchName("Sucursal Centro").products(List.of(p1, p2)).build();

        Product p3 = Product.builder().productName("Café").stock(5).build();
        Branch b2 = Branch.builder().branchName("Sucursal Norte").products(List.of(p3)).build();

        Franchise franchise = Franchise.builder()
                .franchiseName("Supermercado XYZ")
                .branches(List.of(b1, b2))
                .build();

        when(franchiseRepository.getFranchise("Supermercado XYZ"))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(manageFranchiseUseCase.getProductMostStockPerBranch("Supermercado XYZ"))
                .expectNextMatches(map -> {
                    Product topCentro = map.get("Sucursal Centro");
                    Product topNorte = map.get("Sucursal Norte");
                    return topCentro.getProductName().equals("Pan") && topCentro.getStock() == 20
                            && topNorte.getProductName().equals("Café") && topNorte.getStock() == 5;
                })
                .verifyComplete();
    }

    @Test
    void testGetProductMostStockPerBranch_emptyBranches() {
        Franchise franchise = Franchise.builder()
                .franchiseName("Supermercado Vacío")
                .branches(List.of())
                .build();

        when(franchiseRepository.getFranchise("Supermercado Vacío"))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(manageFranchiseUseCase.getProductMostStockPerBranch("Supermercado Vacío"))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testGetProductMostStockPerBranch_notFound() {
        when(franchiseRepository.getFranchise("NoExiste"))
                .thenReturn(Mono.empty());

        StepVerifier.create(manageFranchiseUseCase.getProductMostStockPerBranch("NoExiste"))
                .expectError(BusinessException.class)
                .verify();
    }
}
