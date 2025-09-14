package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class ChangeBranchNameRequest {
    private String oldName;
    private String newName;
}
