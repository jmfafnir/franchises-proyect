package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class ChangeProductStockRequest {
    private String productName;
    private int newStock;
}
