package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class AddBranchRequest {
    private String franchiseName;
    private String branchName;
}
