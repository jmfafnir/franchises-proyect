package co.com.franchises.api.dto;

import lombok.Data;

@Data
public class ChangeProductNameRequest {
    private String oldName;
    private String newName;
}