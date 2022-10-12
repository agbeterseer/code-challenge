package com.seerbit.assessment.service.impl;

import com.seerbit.assessment.SeerbitAssessmentApplication;
import com.seerbit.assessment.dto.request.TransactionRequest;
import com.seerbit.assessment.dto.response.StatisticsResponse;
import com.seerbit.assessment.service.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SeerbitAssessmentApplication.class})
public class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;


    @Before
    public void setUp(){
        transactionService.deleteTransaction();
    }

    @Test
    public void testAddStatistics_withValidStats_added(){
        long current = Instant.now().toEpochMilli();
        TransactionRequest request = new TransactionRequest();

        request.setTimestamp(new Timestamp(current));
        request.setAmount(BigDecimal.valueOf(1.1));
        boolean added = transactionService.createTransaction(request, current);
        Assert.assertTrue(added);
    }

    @Test
    public void testAddStatistics_withNegativeAmount_added(){
        long current = Instant.now().toEpochMilli();
        java.sql.Timestamp ts = java.sql.Timestamp.from(Instant.parse("2018-07-17T09:59:51.312Z"));
        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(ts);
        request.setAmount(BigDecimal.valueOf(-1.1));

        boolean added = transactionService.createTransaction(request, current);
        Assert.assertFalse(added);
    }

    @Test
    public void testAddStatistics_withInPastTimestampMoreThanAMinute_notAdded(){
        long current = Instant.now().toEpochMilli();
        java.sql.Timestamp ts = java.sql.Timestamp.from(Instant.parse("2018-07-17T09:59:51.312Z"));
        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(ts);
        request.setAmount(BigDecimal.valueOf(2.1));
        boolean added = transactionService.createTransaction(request, current);
        Assert.assertFalse(added);
    }

    @Test
    public void testAddStatistics_withInPastTimestampWithinAMinute_created() {
        long current = Instant.now().toEpochMilli();
        long time = convertTimeToLong() -50000;
        Timestamp newTime = new Timestamp(time);

        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(newTime);
        request.setAmount(BigDecimal.valueOf(2.1));
        boolean added = transactionService.createTransaction(request, current);
        Assert.assertFalse(added);

    }

    public long convertTimeToLong() {
        Instant instant = Instant.parse("2018-07-17T09:59:51.312Z");
        return instant.toEpochMilli();
    }
    @Test
    public void testGetStatistics_withAnyData_success() {
        long timestamp = Instant.now().toEpochMilli();
        StatisticsResponse response = transactionService.getTransaction(timestamp);
        Assert.assertEquals(0, response.getCount());
        Assert.assertEquals(0, response.getMax().doubleValue(), 0);
        Assert.assertEquals(0, response.getMin().doubleValue(), 0);
        Assert.assertEquals(0, response.getAvg().doubleValue(), 0);
    }

    @Test
    public void testAddAndGetStatistics_withValidTimestampMultipleThread_success() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        int n = 0;
        BigDecimal amount = BigDecimal.valueOf(1.00);
        int count = 60000;
        long timestamp = Instant.now().toEpochMilli();
        long requestTime = timestamp;
        while(n<count) {
            // Time frame is managed from 0 to 59, for cache size 60.
            if(timestamp - requestTime >= 59000) {
                requestTime = timestamp;
            }
            TransactionRequest request = new TransactionRequest();
            request.setTimestamp(new Timestamp(timestamp));
            request.setAmount(amount);

            executorService.submit(() -> transactionService.createTransaction(request, timestamp));
            n++;

            double newAmount =  amount.doubleValue();
            newAmount++;
            amount = BigDecimal.valueOf(newAmount);
            requestTime -= 1;
        }

        executorService.shutdown();
        Thread.sleep(1000);
        StatisticsResponse response = transactionService.getTransaction(timestamp);
        Assert.assertEquals(count, response.getCount());
        Assert.assertEquals(count, response.getMax().doubleValue(), 0);
        Assert.assertEquals(1, response.getMin().doubleValue(), 0);

    }


}