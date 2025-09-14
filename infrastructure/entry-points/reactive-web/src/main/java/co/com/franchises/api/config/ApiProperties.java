package co.com.franchises.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "entries.reactive-web")
public class ApiProperties {
    private String pathBase;

    private RequestFranchise requestFranchise;
    private RequestBranch requestBranch;
    private RequestProduct requestProduct;

    @Data
    public static class RequestFranchise {
        private String requestAdd;
        private String requestChangeName;
        private String requestGetProducts;
        private String requestAddBranch;
    }

    @Data
    public static class RequestBranch {
        private String requestChangeName;
        private String requestAddProduct;
        private String requestDeleteProduct;
    }

    @Data
    public static class RequestProduct {
        private String requestChangeName;
        private String requestChangeStock;
    }
}
