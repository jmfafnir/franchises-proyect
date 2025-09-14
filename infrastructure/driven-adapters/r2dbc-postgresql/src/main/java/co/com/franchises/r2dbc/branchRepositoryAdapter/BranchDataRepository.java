package co.com.franchises.r2dbc.branchRepositoryAdapter;

import co.com.franchises.r2dbc.dto.BranchData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BranchDataRepository extends ReactiveCrudRepository<BranchData,String>,
        ReactiveQueryByExampleExecutor<BranchData>{

    Flux<BranchData> findAllByFranchiseId(String franchiseId);

}
