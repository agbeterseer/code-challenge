package com.seerbit.assessment.cache;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class TransactionStatisticsCache<Long, TransactionStatistics> extends LinkedHashMap<Long, TransactionStatistics> {
    private static final int MIN_CACHE_CAPACITY = 30;
    private static final int MAX_CACHE_CAPACITY = 30000;
    private int capacity = MIN_CACHE_CAPACITY;

    public TransactionStatisticsCache(){
        super();
    }

    public TransactionStatisticsCache(int capacity){
        super();
        if(capacity < 30){
            capacity = MIN_CACHE_CAPACITY;
        }else if(capacity > MAX_CACHE_CAPACITY){
            capacity = MAX_CACHE_CAPACITY;
        }else if(capacity % 3 !=0 ){
            int mod = capacity % 3;
            capacity +=(3-mod);
        }
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, TransactionStatistics> oldest){
        return this.size() > capacity;
    }

    public int getCapacity(){
        return capacity;
    }



}
