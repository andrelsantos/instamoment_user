package io.instamoment.service.util;

import io.instamoment.service.exception.ApiExceptionHandler;

public class ErrorMessageUtil {

    public static ApiExceptionHandler.Error getUserNotFound(){
        return new ApiExceptionHandler.Error("Usuário não encontrado", "");
    }

    public static ApiExceptionHandler.Error getCodeAccessInvalid(){
        return new ApiExceptionHandler.Error("Código de acesso inválido", "");
    }
}
