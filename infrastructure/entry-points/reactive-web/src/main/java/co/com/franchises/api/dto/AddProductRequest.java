package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class AddProductRequest {
    private String branchName;
    private String productName;
    private int stock;
}
