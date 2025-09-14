package co.com.franchises.r2dbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("product")
public class ProductData {

    @Id
    @Column("id")
    private String id;

    @Column("branch_id")
    private String branchId;

    @Column("stock")
    private Integer stock;
}
