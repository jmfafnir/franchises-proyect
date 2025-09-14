package co.com.franchises.r2dbc.franchiseRepositoryAdapter;


import co.com.franchises.r2dbc.dto.FranchiseData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseDataRepository extends ReactiveCrudRepository<FranchiseData, String>,
        ReactiveQueryByExampleExecutor<FranchiseData> {

}
