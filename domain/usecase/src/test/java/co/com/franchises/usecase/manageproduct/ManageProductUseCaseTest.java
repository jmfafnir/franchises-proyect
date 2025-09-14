package co.com.franchises.usecase.manageproduct;

import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.gateways.ProductRepository;
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
class ManageProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ManageProductUseCase manageProductUseCase;

    @BeforeEach
    void setup() {

    }

    @Test
    void testUpDateNameProduct_success() {
        String oldName = "Leche";
        String newName = "Leche Deslactosada";

        Product updated = Product.builder()
                .productName(newName)
                .stock(10)
                .build();

        when(productRepository.UpDateName(oldName, newName))
                .thenReturn(Mono.just(updated));

        StepVerifier.create(manageProductUseCase.upDateNameProduct(oldName, newName))
                .expectNextMatches(product -> product.getProductName().equals("Leche Deslactosada"))
                .verifyComplete();
    }

    @Test
    void testUpDateStock_success() {
        String productName = "Pan";
        int newStock = 50;

        Product updated = Product.builder()
                .productName(productName)
                .stock(newStock)
                .build();

        when(productRepository.upDateProduct(productName, newStock))
                .thenReturn(Mono.just(updated));

        StepVerifier.create(manageProductUseCase.upDateStock(productName, newStock))
                .expectNextMatches(product ->
                        product.getProductName().equals("Pan") &&
                                product.getStock() == 50
                )
                .verifyComplete();
    }



}
