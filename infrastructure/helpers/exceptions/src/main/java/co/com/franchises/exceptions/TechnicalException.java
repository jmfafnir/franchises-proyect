package co.com.franchises.exceptions;

import co.com.franchises.exceptions.enums.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {
    private final TechnicalExceptionEnum technicalExceptionEnum;

    public TechnicalException(Throwable cause, TechnicalExceptionEnum technicalExceptionEnum){
        super(technicalExceptionEnum.getMessage(), cause);
        this.technicalExceptionEnum = technicalExceptionEnum;
    }

    public TechnicalException(TechnicalExceptionEnum technicalExceptionEnum){
        super(technicalExceptionEnum.getMessage());
        this.technicalExceptionEnum = technicalExceptionEnum;
    }
}
