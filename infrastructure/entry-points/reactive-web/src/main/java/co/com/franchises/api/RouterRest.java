package co.com.franchises.api;

import co.com.franchises.api.config.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ApiProperties.class})
public class RouterRest {

    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route().POST(apiProperties.getRequestFranchise().getRequestAdd(),
                        handler::addFranchise)
                .POST(apiProperties.getRequestFranchise().getRequestChangeName(),
                        handler::changeFranchiseName)
                .POST(apiProperties.getRequestFranchise().getRequestGetProducts(),
                        handler::getInventory)
                .POST(apiProperties.getRequestFranchise().getRequestAddBranch(),
                        handler::addBranch)
                .POST(apiProperties.getRequestBranch().getRequestChangeName(),
                        handler::changeBranchName)
                .POST(apiProperties.getRequestBranch().getRequestAddProduct(),
                        handler::addProduct)
                .POST(apiProperties.getRequestBranch().getRequestDeleteProduct(),
                        handler::deleteProduct)
                .POST(apiProperties.getRequestProduct().getRequestChangeName(),
                        handler::changeProductName)
                .POST(apiProperties.getRequestProduct().getRequestChangeStock(),
                        handler::changeProductStock)
                .build();
    }
}
