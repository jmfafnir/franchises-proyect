package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class ChangeFranchiseNameRequest {
    private String oldName;
    private String newName;
}
