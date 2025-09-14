package co.com.franchises.r2dbc;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.model.product.Product;
import co.com.franchises.r2dbc.dto.ProductData;
import co.com.franchises.r2dbc.productRepositoryAdapter.ProductDataRepository;
import co.com.franchises.r2dbc.productRepositoryAdapter.ProductRepositoryAdapter;
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
class ProductRepositoryAdapterTest {
    @Mock
    ProductDataRepository productRepository;

    @Mock
    ObjectMapper mapper;

    ProductRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductRepositoryAdapter(productRepository, mapper);
    }

    @Test
    void upDateProduct_whenProductExists_updatesStock() {
        ProductData productData = ProductData.builder().id("P001").stock(10).build();
        ProductData updatedProductData = ProductData.builder().id("P001").stock(20).build();
        Product productEntity = Product.builder().productName("P001").stock(20).build();

        when(productRepository.findById("P001")).thenReturn(Mono.just(productData));
        when(productRepository.save(any(ProductData.class))).thenReturn(Mono.just(updatedProductData));
        when(mapper.map(updatedProductData, Product.class)).thenReturn(productEntity);

        StepVerifier.create(adapter.upDateProduct("P001", 20))
                .expectNextMatches(p -> p.getProductName().equals("P001") && p.getStock() == 20)
                .verifyComplete();
    }

    @Test
    void upDateProduct_whenProductNotFound_throwsException() {
        when(productRepository.findById("P001")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.upDateProduct("P001", 20))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.BUSINESS_PRODUCT_NOT_FOUND)
                .verify();
    }

    @Test
    void UpDateName_whenProductExists_updatesName() {
        ProductData productData = ProductData.builder().id("P001").stock(10).build();
        ProductData updatedProductData = ProductData.builder().id("P002").stock(10).build();
        Product productEntity = Product.builder().productName("P002").stock(10).build();

        when(productRepository.findById("P001")).thenReturn(Mono.just(productData));
        when(productRepository.save(any(ProductData.class))).thenReturn(Mono.just(updatedProductData));
        when(mapper.map(updatedProductData, Product.class)).thenReturn(productEntity);

        StepVerifier.create(adapter.UpDateName("P001", "P002"))
                .expectNextMatches(p -> p.getProductName().equals("P002") && p.getStock() == 10)
                .verifyComplete();
    }

    @Test
    void UpDateName_whenProductNotFound_throwsException() {
        when(productRepository.findById("P001")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.UpDateName("P001", "P002"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBussinesExceptionEnum() ==
                                        BussinesExceptionEnum.BUSINESS_PRODUCT_NOT_FOUND)
                .verify();
    }
}
