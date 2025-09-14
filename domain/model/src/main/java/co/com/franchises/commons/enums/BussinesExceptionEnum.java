package co.com.franchises.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BussinesExceptionEnum {

    BUSINESS_ADD_FRANCHISE("Formato o Datos invalidos en la franquicia",400),
    BRANCH_ALREADY_EXISTS("La sucursal ya existe",400),
    PRODUCT_ALREADY_EXISTS("La sucursal ya existe",400),
    BUSINESS_FRANCHISE_NOT_FOUND("La franquisia no existe" , 404),
    BUSINESS_BRANCH_NOT_FOUND("La sucursal no existe" , 404),
    BUSINESS_PRODUCT_NOT_FOUND("La sucursal no existe" , 404),
    BUSINESS_GET_PRODUCTS_MAX_STOCK("La Franquisia no cuenta con productos en las sucursales",404);


    private final String message;
    private final int code;

}
