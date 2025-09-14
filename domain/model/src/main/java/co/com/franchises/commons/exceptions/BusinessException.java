package co.com.franchises.commons.exceptions;

import co.com.franchises.commons.enums.BussinesExceptionEnum;
import lombok.Getter;

@Getter
public class BusinessException extends  RuntimeException {

    public final BussinesExceptionEnum bussinesExceptionEnum;


    public BusinessException(BussinesExceptionEnum bussinesExceptionEnum){
        super(bussinesExceptionEnum.getMessage());
        this.bussinesExceptionEnum = bussinesExceptionEnum;
    }

}
