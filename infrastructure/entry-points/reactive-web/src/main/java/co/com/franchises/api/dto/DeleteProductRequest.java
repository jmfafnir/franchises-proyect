package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class DeleteProductRequest {
    private String branchName;
    private String productName;
}