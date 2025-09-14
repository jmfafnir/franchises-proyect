package co.com.franchises.model.product;

import co.com.franchises.model.branch.Branch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        Product p1 = Product.builder().productName("Leche").stock(10).build();
        Branch branch = new Branch("Sucursal Centro", List.of(p1));

        assertEquals("Sucursal Centro", branch.getBranchName());
        assertEquals(1, branch.getProducts().size());
        assertEquals("Leche", branch.getProducts().get(0).getProductName());
    }

    @Test
    void testBuilder() {
        Product p1 = Product.builder().productName("Pan").stock(5).build();
        Branch branch = Branch.builder()
                .branchName("Sucursal Norte")
                .products(List.of(p1))
                .build();

        assertNotNull(branch);
        assertEquals("Sucursal Norte", branch.getBranchName());
        assertEquals("Pan", branch.getProducts().get(0).getProductName());
    }

    @Test
    void testSettersAndToBuilder() {
        Branch branch = Branch.builder()
                .branchName("Sucursal Sur")
                .products(List.of())
                .build();


        branch.setBranchName("Sucursal Sur Actualizada");
        assertEquals("Sucursal Sur Actualizada", branch.getBranchName());


        Branch updated = branch.toBuilder()
                .branchName("Sucursal Renombrada")
                .build();

        assertEquals("Sucursal Renombrada", updated.getBranchName());
        assertEquals("Sucursal Sur Actualizada", branch.getBranchName());
    }
}
