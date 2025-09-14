package co.com.franchises.exceptions.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {
    TECHNICAL_EXCEPTION_ERROR("Error tecnico interno",500);
    private final String message;
    private final int code;
}
