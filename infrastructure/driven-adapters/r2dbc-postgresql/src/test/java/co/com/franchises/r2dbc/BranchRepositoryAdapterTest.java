package co.com.franchises.r2dbc;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.product.Product;
import co.com.franchises.r2dbc.branchRepositoryAdapter.BranchDataRepository;
import co.com.franchises.r2dbc.branchRepositoryAdapter.BranchRepositoryAdapter;
import co.com.franchises.r2dbc.dto.BranchData;
import co.com.franchises.r2dbc.dto.ProductData;
import co.com.franchises.r2dbc.productRepositoryAdapter.ProductDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchRepositoryAdapterTest {
    @Mock
    BranchDataRepository branchRepository;

    @Mock
    ProductDataRepository productDataRepository;

    @Mock
    ObjectMapper mapper;

    BranchRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BranchRepositoryAdapter(branchRepository, productDataRepository, mapper);
    }

    @Test
    void upDateNameBranch_whenBranchExists_updatesName() {
        BranchData oldBranch = BranchData.builder().id("B001").build();
        BranchData updatedBranch = BranchData.builder().id("B002").build();
        Branch branchEntity = Branch.builder().branchName("B002").build();

        when(branchRepository.findById("B001")).thenReturn(Mono.just(oldBranch));
        when(branchRepository.save(any(BranchData.class))).thenReturn(Mono.just(updatedBranch));
        when(mapper.map(updatedBranch, Branch.class)).thenReturn(branchEntity);

        StepVerifier.create(adapter.upDateNameBranch("B001", "B002"))
                .expectNextMatches(b -> b.getBranchName().equals("B002"))
                .verifyComplete();
    }

    @Test
    void upDateNameBranch_whenBranchNotFound_throwsException() {
        when(branchRepository.findById("B001")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.upDateNameBranch("B001", "B002"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.BUSINESS_BRANCH_NOT_FOUND)
                .verify();
    }

    @Test
    void addProduct_whenBranchNotFound_throwsException() {
        Product product = Product.builder().productName("P001").stock(10).build();
        when(branchRepository.findById("B001")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.addProduct("B001", product))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.BUSINESS_BRANCH_NOT_FOUND)
                .verify();
    }

    @Test
    void addProduct_whenProductAlreadyExists_throwsException() {
        Product product = Product.builder().productName("P001").stock(10).build();
        BranchData branch = BranchData.builder().id("B001").build();
        ProductData existingProduct = ProductData.builder().id("P001").branchId("B001").stock(10).build();

        when(branchRepository.findById("B001")).thenReturn(Mono.just(branch));
        when(productDataRepository.findById("P001")).thenReturn(Mono.just(existingProduct));
        when(productDataRepository.save(any(ProductData.class))).thenReturn(Mono.just(existingProduct));

        StepVerifier.create(adapter.addProduct("B001", product))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.PRODUCT_ALREADY_EXISTS)
                .verify();
    }

    @Test
    void addProduct_success() {
        Product product = Product.builder().productName("P001").stock(10).build();
        BranchData branch = BranchData.builder().id("B001").build();
        ProductData savedProduct = ProductData.builder().id("P001").branchId("B001").stock(10).build();

        when(branchRepository.findById("B001")).thenReturn(Mono.just(branch));
        when(productDataRepository.findById("P001")).thenReturn(Mono.empty());
        when(productDataRepository.save(any(ProductData.class))).thenReturn(Mono.just(savedProduct));

        StepVerifier.create(adapter.addProduct("B001", product))
                .expectNextMatches(p -> p.getProductName().equals("P001") && p.getStock() == 10)
                .verifyComplete();
    }

    @Test
    void deleteProduct_whenBranchNotFound_throwsException() {
        when(branchRepository.findById("B001")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteProduct("B001", "P001"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.BUSINESS_BRANCH_NOT_FOUND)
                .verify();
    }

    @Test
    void deleteProduct_whenProductNotFound_throwsException() {
        BranchData branch = BranchData.builder().id("B001").build();
        when(branchRepository.findById("B001")).thenReturn(Mono.just(branch));
        when(productDataRepository.findById("P001")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteProduct("B001", "P001"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.BUSINESS_PRODUCT_NOT_FOUND)
                .verify();
    }

    @Test
    void deleteProduct_success() {
        BranchData branch = BranchData.builder().id("B001").build();
        ProductData product = ProductData.builder().id("P001").branchId("B001").stock(10).build();

        when(branchRepository.findById("B001")).thenReturn(Mono.just(branch));
        when(productDataRepository.findById("P001")).thenReturn(Mono.just(product));
        when(productDataRepository.delete(product)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteProduct("B001", "P001"))
                .expectNext(true)
                .verifyComplete();
    }
}
