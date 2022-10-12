package com.seerbit.assessment.exception;

import com.seerbit.assessment.dto.response.ResponseHelper;
import org.apache.logging.log4j.util.Strings;

public class ErrorResponse extends ResponseHelper {
    public ErrorResponse(String message){
        super(false, message, Strings.EMPTY);
    }
}

