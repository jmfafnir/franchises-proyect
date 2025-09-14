package co.com.franchises.model.franchise;

import co.com.franchises.model.branch.Branch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FranchiseTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        Branch branch = Branch.builder()
                .branchName("Sucursal Centro")
                .products(List.of())
                .build();

        Franchise franchise = new Franchise("Supermercado XYZ", List.of(branch));

        assertEquals("Supermercado XYZ", franchise.getFranchiseName());
        assertEquals(1, franchise.getBranches().size());
        assertEquals("Sucursal Centro", franchise.getBranches().get(0).getBranchName());
    }

    @Test
    void testBuilder() {
        Branch branch = Branch.builder()
                .branchName("Sucursal Norte")
                .products(List.of())
                .build();

        Franchise franchise = Franchise.builder()
                .franchiseName("Supermercado ABC")
                .branches(List.of(branch))
                .build();

        assertNotNull(franchise);
        assertEquals("Supermercado ABC", franchise.getFranchiseName());
        assertEquals("Sucursal Norte", franchise.getBranches().get(0).getBranchName());
    }

    @Test
    void testSettersAndToBuilder() {
        Franchise franchise = Franchise.builder()
                .franchiseName("Supermercado Inicial")
                .branches(List.of())
                .build();

        franchise.setFranchiseName("Supermercado Actualizado");
        assertEquals("Supermercado Actualizado", franchise.getFranchiseName());

        Franchise updated = franchise.toBuilder()
                .franchiseName("Supermercado Clonado")
                .build();

        assertEquals("Supermercado Clonado", updated.getFranchiseName());
        assertEquals("Supermercado Actualizado", franchise.getFranchiseName());
    }
}
