package co.com.franchises.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BussinesExceptionEnum {

    BUSINESS_ADD_FRANCHISE("Formato o Datos invalidos en la franquicia",400),
    BUSINESS_GET_PRODUCTS_MAX_STOCK("La Franquisia no cuenta con productos en las sucursales",404);


    private final String message;
    private final int code;

}
