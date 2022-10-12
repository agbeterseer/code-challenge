package com.seerbit.assessment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHelper {
    private Date timeStamp = new Date();
    private Boolean status;
    private String message;
    private Object data;

    public ResponseHelper(Boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
