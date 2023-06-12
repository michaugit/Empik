package com.test.empik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
public class ExternalServiceErrorException extends RuntimeException {
        public ExternalServiceErrorException(String message){
            super(message);
        }

        public ExternalServiceErrorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
