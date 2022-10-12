package com.seerbit.assessment.dto.response;

import static com.seerbit.assessment.util.Constant.*;

public class SuccessResponse extends ResponseHelper {
    public SuccessResponse(String message, Object data){
        super(true, message, data);
    }

    public SuccessResponse(Object data){
        super(true, SUCCESS_MESSAGE, data);
    }

}
