package co.com.franchises.model.franchise.gateways;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> getFranchise(String franchiseName);
    Mono<Franchise> addFranchise(Franchise franchise);
    Mono<Franchise> upDateNameFranchise(String oldName, String newName);
    Mono<Franchise> addBranch(String  franchiseName, Branch branch);



}
