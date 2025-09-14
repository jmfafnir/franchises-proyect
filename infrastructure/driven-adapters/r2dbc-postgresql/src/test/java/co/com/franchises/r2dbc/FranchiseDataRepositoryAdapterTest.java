package co.com.franchises.r2dbc;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.product.Product;
import co.com.franchises.r2dbc.branchRepositoryAdapter.BranchDataRepository;
import co.com.franchises.r2dbc.dto.BranchData;
import co.com.franchises.r2dbc.dto.FranchiseData;
import co.com.franchises.r2dbc.dto.ProductData;
import co.com.franchises.r2dbc.franchiseRepositoryAdapter.FranchiseDataRepository;
import co.com.franchises.r2dbc.franchiseRepositoryAdapter.FranchiseRepositoryAdapter;
import co.com.franchises.r2dbc.productRepositoryAdapter.ProductDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseDataRepositoryAdapterTest {

    @Mock
    private FranchiseDataRepository franchiseRepository;

    @Mock
    private BranchDataRepository branchRepository;

    @Mock
    private ProductDataRepository productRepository;

    @Mock
    private ObjectMapper mapper;

    private FranchiseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchiseRepositoryAdapter(franchiseRepository, branchRepository, productRepository, mapper);
    }

    @Test
    void getFranchise_returnsFranchiseWithBranchesAndProducts() {
        FranchiseData franchiseData = FranchiseData.builder().id("FR123").build();
        BranchData branchData = BranchData.builder().id("B001").franchiseId("FR123").build();
        ProductData productData = ProductData.builder().id("P001").branchId("B001").stock(10).build();
        Product product = Product.builder().productName("P001").stock(10).build();

        when(franchiseRepository.findById("FR123")).thenReturn(Mono.just(franchiseData));
        when(branchRepository.findAllByFranchiseId("FR123")).thenReturn(Flux.just(branchData));
        when(productRepository.findAllByBranchId("B001")).thenReturn(Flux.just(productData));
        when(mapper.map(productData, Product.class)).thenReturn(product);


        StepVerifier.create(adapter.getFranchise("FR123"))
                .expectNextMatches(f -> f.getFranchiseName().equals("FR123") &&
                        f.getBranches().size() == 1 &&
                        f.getBranches().get(0).getProducts().size() == 1)
                .verifyComplete();
    }

    @Test
    void addFranchise_savesAndReturnsFranchise() {
        Franchise franchise = Franchise.builder().franchiseName("FR123").build();
        FranchiseData franchiseData = FranchiseData.builder().id("FR123").build();
        when(mapper.map(franchiseData, Franchise.class)).thenReturn(franchise);
        when(franchiseRepository.save(any())).thenReturn(Mono.just(franchiseData));

        StepVerifier.create(adapter.addFranchise(franchise))
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void upDateNameFranchise_updatesIdAndReturnsFranchise() {
        FranchiseData oldData = FranchiseData.builder().id("OLD").build();
        FranchiseData newData = FranchiseData.builder().id("NEW").build();
        Franchise updatedFranchise = Franchise.builder().franchiseName("NEW").build();

        when(franchiseRepository.findById("OLD")).thenReturn(Mono.just(oldData));
        when(franchiseRepository.save(newData)).thenReturn(Mono.just(newData));
        when(mapper.map(newData, Franchise.class)).thenReturn(updatedFranchise);

        StepVerifier.create(adapter.upDateNameFranchise("OLD", "NEW"))
                .expectNext(updatedFranchise)
                .verifyComplete();
    }

    @Test
    void addBranch_whenBranchAlreadyExists_returnsError() {
        Branch branch = Branch.builder().branchName("B001").build();
        FranchiseData franchiseData = FranchiseData.builder().id("FR123").build();
        BranchData existingBranchData = BranchData.builder().id("B001").franchiseId("FR123").build();

        when(franchiseRepository.findById("FR123")).thenReturn(Mono.just(franchiseData));
        when(branchRepository.findById("B001")).thenReturn(Mono.just(existingBranchData));
        when(branchRepository.save(any(BranchData.class))).thenReturn(Mono.just(existingBranchData));
        when(branchRepository.findAllByFranchiseId(anyString())).thenReturn(Flux.just(existingBranchData));

        StepVerifier.create(adapter.addBranch("FR123", branch))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() == BussinesExceptionEnum.BRANCH_ALREADY_EXISTS)
                .verify();
    }

    @Test
    void addBranch_whenBranchDoesNotExist_savesBranchAndReturnsFranchise() {
        Branch branch = Branch.builder().branchName("B002").build();
        FranchiseData franchiseData = FranchiseData.builder().id("FR123").build();
        BranchData savedBranchData = BranchData.builder().id("B002").franchiseId("FR123").build();
        Branch domainBranch = Branch.builder().branchName("B002").products(Collections.emptyList()).build();

        when(franchiseRepository.findById("FR123")).thenReturn(Mono.just(franchiseData));
        when(branchRepository.findById("B002")).thenReturn(Mono.empty());
        when(branchRepository.save(any())).thenReturn(Mono.just(savedBranchData));
        when(branchRepository.findAllByFranchiseId("FR123")).thenReturn(Flux.just(savedBranchData));

        StepVerifier.create(adapter.addBranch("FR123", branch))
                .expectNextMatches(f -> f.getFranchiseName().equals("FR123") &&
                        f.getBranches().size() == 1 &&
                        f.getBranches().get(0).getBranchName().equals("B002"))
                .verifyComplete();
    }
}
