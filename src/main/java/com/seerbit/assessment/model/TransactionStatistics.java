package com.seerbit.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatistics {
    private Lock lock = new ReentrantLock();
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal avg = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
    private long count = 0;

    private TransactionStatistics(TransactionStatistics statistics) {
        this.sum = statistics.sum;
        this.max = statistics.max;
        this.min = statistics.min;
        this.count= statistics.count;
    }

    public void updateTransactionStatistics(BigDecimal amount){
        double sum = 0.00;
        try{
            lock.lock();
            sum += roundUpAmount(amount).doubleValue();
            this.setSum(BigDecimal.valueOf(sum));
            count++;
            min = min.doubleValue() < amount.doubleValue() ? min : amount;
            max = max.doubleValue() > amount.doubleValue() ? max : amount;
        }finally {
            lock.unlock();
        }
    }

    public TransactionStatistics getTransactionStatistics(){
        try{
            lock.lock();
            return new TransactionStatistics(this);
        }finally {
            lock.unlock();
    }

    }

    private BigDecimal roundUpAmount(BigDecimal amount){
        BigDecimal newAmount = BigDecimal.valueOf(amount.doubleValue());
        return newAmount.setScale(2, RoundingMode.HALF_UP);
    }

}
