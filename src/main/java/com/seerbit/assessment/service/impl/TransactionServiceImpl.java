package com.seerbit.assessment.service.impl;

import com.seerbit.assessment.cache.TransactionStatisticsCache;
import com.seerbit.assessment.dto.request.TransactionRequest;
import static com.seerbit.assessment.util.Constant.*;

import com.seerbit.assessment.dto.response.StatisticsResponse;
import com.seerbit.assessment.exception.CustomException;
import com.seerbit.assessment.model.TransactionStatistics;
import com.seerbit.assessment.service.TransactionService;

import com.seerbit.assessment.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionStatisticsCache<Long, TransactionStatistics> cache;


    @Autowired
    public TransactionServiceImpl(TransactionStatisticsCache<Long, TransactionStatistics> cache) {
        this.cache = cache;
    }

    @Override
    public boolean createTransaction(TransactionRequest transactionRequest, long timestamp) {
        transactionRequest.setAmount(Helper.roundUpAmount(transactionRequest.getAmount()));

        long requestTime = transactionRequest.getTimestamp().getTime();
        long waitTime = timestamp - requestTime;

        if(waitTime < 0 || waitTime >= THIRTY_SEC_MLS){
            return false;
        }else{
            Long key = getKeyFromTimestamp(requestTime);
            TransactionStatistics statistics = cache.get(key);
            if(statistics == null){
                synchronized (cache){
                    statistics = cache.get(key);
                    if(statistics == null){
                        statistics = new TransactionStatistics();
                        cache.put(key, statistics);
                    }
                }
            }
            statistics.updateTransactionStatistics(transactionRequest.getAmount());
        }

      return true;
    }

    @Override
    public StatisticsResponse getTransaction(long timestamp) {
        Map<Long, TransactionStatistics> copy = cache.entrySet().parallelStream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getTransactionStatistics()));
        return getTransactionStatistics(copy, timestamp);
    }

    public StatisticsResponse getTransactionStatistics(Map<Long, TransactionStatistics> map, long timestamp){
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal avg;
        BigDecimal max = BigDecimal.ZERO;
        BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
        long count = 0;
        Long key = getKeyFromTimestamp(timestamp);

        for(Map.Entry<Long, TransactionStatistics> mapp : map.entrySet()){
            Long ekey = mapp.getKey();
            long timeFrame = key - ekey;
            if(timeFrame >= 0 && timeFrame < cache.getCapacity()){
                TransactionStatistics data = mapp.getValue();
                if(data.getCount() > 0){
                    double eSum = 0.00;
                    eSum += data.getSum().doubleValue();
                    sum = BigDecimal.valueOf(eSum);
                    min = min.doubleValue() < data.getMin().doubleValue() ? min : data.getMin();
                    max = max.doubleValue() > data.getMax().doubleValue() ? max : data.getMax();
                    count += data.getCount();
                }
            }
        }
        if(count == 0) {
            min = BigDecimal.valueOf(0);
            avg = BigDecimal.valueOf(0);
        } else {
            avg = BigDecimal.valueOf(sum.doubleValue() / count);
        }

        return buildResponse(sum,avg,max,min,count);
    }


    private StatisticsResponse buildResponse(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count){
        StatisticsResponse response = new StatisticsResponse();
        response.setSum(sum);
        response.setAvg(avg);
        response.setCount(count);
        response.setMax(max);
        response.setMin(min);

        return response;

    }

    private Long getKeyFromTimestamp(Long timestamp){
        return (timestamp * cache.getCapacity())  / THIRTY_SEC_MLS;
    }

    @Override
    public void deleteTransaction() {
        cache.clear();
    }
}
