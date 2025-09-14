package co.com.franchises.r2dbc.productRepositoryAdapter;

import co.com.franchises.r2dbc.dto.ProductData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductDataRepository extends ReactiveCrudRepository<ProductData, String>,
        ReactiveQueryByExampleExecutor<ProductData> {

    Flux<ProductData> findAllByBranchId(String branchId);
}
