package co.com.franchises.api.util;

import co.com.franchises.commons.exceptions.BusinessException;
import co.com.franchises.exceptions.TechnicalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Mono<ServerResponse> handleBusinessException(BusinessException ex) {
        return ServerResponse
                .status(HttpStatusCode.valueOf(ex.getBussinesExceptionEnum().getCode()))
                .bodyValue(Map.of(
                        "message", ex.getMessage()
                ));
    }


    @ExceptionHandler(TechnicalException.class)
    public Mono<ServerResponse> handleTechnicalException(TechnicalException ex) {
        return ServerResponse
                .status(HttpStatusCode.valueOf(ex.getTechnicalExceptionEnum().getCode()))
                .bodyValue(Map.of(
                        "message", "Ha ocurrido un error inesperado"
                ));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleException(Exception ex) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(Map.of(
                        "error", "INTERNAL_ERROR",
                        "message", "Ha ocurrido un error inesperado"
                ));
    }
}
