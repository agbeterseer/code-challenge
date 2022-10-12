package com.seerbit.assessment.dto.response;

import com.seerbit.assessment.util.Helper;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class StatisticsResponse {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;

    public StatisticsResponse(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count) {
        this.sum = Helper.roundUpAmount(sum);
        this.avg = Helper.roundUpAmount(avg);
        this.max = Helper.roundUpAmount(max);
        this.min = Helper.roundUpAmount(min);
        this.count = count;
    }

    public StatisticsResponse(){

    }

}
