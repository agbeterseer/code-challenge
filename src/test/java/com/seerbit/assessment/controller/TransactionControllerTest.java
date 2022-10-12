package com.seerbit.assessment.controller;

import com.seerbit.assessment.SeerbitAssessmentApplication;

import com.seerbit.assessment.dto.request.TransactionRequest;
import com.seerbit.assessment.dto.response.StatisticsResponse;
import com.seerbit.assessment.service.TransactionService;
import com.seerbit.assessment.service.impl.TransactionServiceImplTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SeerbitAssessmentApplication.class})
public class TransactionControllerTest {

    @Autowired
    private TransactionController controller;
    @Autowired
    private TransactionService transactionService;


    @Before
    public void setUp() {
        transactionService.deleteTransaction();
    }

    @Test
    public void testAddStatistics_withValidStats_created(){
        long current = Instant.now().toEpochMilli();
        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(new Timestamp(current));
        request.setAmount(BigDecimal.valueOf(17.1764));

        ResponseEntity<?> responseEntity = controller.createTransaction(request);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testAddTransactions_withNegativeAmount_created(){
        long current = Instant.now().toEpochMilli();
        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(new Timestamp(current));
        request.setAmount(BigDecimal.valueOf(-12.1983));

        ResponseEntity<?> responseEntity = controller.createTransaction(request);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testAddTransactions_withInPastTimestampMoreThanAMinute_noContent(){

        TransactionServiceImplTest transactionServiceImplTest = new TransactionServiceImplTest();
        long time = transactionServiceImplTest.convertTimeToLong() - 30000;


        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(new Timestamp(time));
        request.setAmount(BigDecimal.valueOf(23.1233));

        ResponseEntity<?> responseEntity = controller.createTransaction(request);

        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testAddTransactions_withInPastTimestampWithinAMinute_created(){

        TransactionServiceImplTest transactionServiceImplTest = new TransactionServiceImplTest();
        long time = transactionServiceImplTest.convertTimeToLong() - 50000;

        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(new Timestamp(time));
        request.setAmount(BigDecimal.valueOf(2.1));

        ResponseEntity<?> responseEntity = controller.createTransaction(request);
        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testAddAndGetStatistics_withInValidTimestampWithinAMinuteWithSameTime_success() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int n = 0;
       // double amount = 1.0;
        BigDecimal amount = BigDecimal.valueOf(12.3343);
        int count = 1000;
        while(n<count) {
           // StatisticsRequest request = StatisticsRequestBuilder.createStatisticsRequest().withAmount(amount).withTimestamp(Instant.now().toEpochMilli()).build();
            TransactionRequest request = new TransactionRequest();
            request.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            request.setAmount(amount);

            executorService.submit(() -> controller.createTransaction(request));
            n++;
            double newAmount =  amount.doubleValue();
            newAmount += 1;
            amount = roundUpAmount(BigDecimal.valueOf(newAmount));
        }

        executorService.shutdown();
        Thread.sleep(1000);
        ResponseEntity<?> response = controller.getTransactionStatics();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(count, ((StatisticsResponse) Objects.requireNonNull(response.getBody())).getCount());
    }

    @Test
    public void testAddAndGetStatistics_withInValidTimestampWithinAMinuteWithDifferentTime_success() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int n = 0;
        //double amount = 1.0;
        BigDecimal amount = BigDecimal.valueOf(12.3343);
        int count = 50000;
        long timestamp = Instant.now().toEpochMilli();
        while(n<count) {
           // StatisticsRequest request = StatisticsRequestBuilder.createStatisticsRequest().withAmount(amount).withTimestamp(timestamp).build();
            TransactionRequest request = new TransactionRequest();
            request.setTimestamp(new Timestamp(timestamp));
            request.setAmount(amount);

            executorService.submit(() -> controller.createTransaction(request));
            n++;
            double newAmount =  amount.doubleValue();
            newAmount += 1;
            amount = BigDecimal.valueOf(newAmount);
            timestamp -= 1;
        }

        executorService.shutdown();
        Thread.sleep(1000);
        ResponseEntity<?> response = controller.getTransactionStatics();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(count, ((StatisticsResponse) Objects.requireNonNull(response.getBody())).getCount());
    }

    @Test
    public void testAddAndGetStatistics_withInValidAndOutdatedTimestamp_success() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int n = 0;
        BigDecimal amount = BigDecimal.valueOf(12.3343);
        int count = 1000;
        long timestamp = Instant.now().toEpochMilli();
        while(n<count) {
           // StatisticsRequest request = StatisticsRequestBuilder.createStatisticsRequest().withAmount(amount).withTimestamp(timestamp).build();
            TransactionRequest request = new TransactionRequest();
            request.setTimestamp(new Timestamp(timestamp));
            request.setAmount(amount);
            executorService.submit(() -> controller.createTransaction(request));
            n++;

            double newAmount =  amount.doubleValue();
            newAmount += 1;
            amount = BigDecimal.valueOf(newAmount);
            timestamp -= 1;
        }

        Thread.sleep(1000);
        timestamp -= 60000;
        n = 0;
        while(n<count) {
            //StatisticsRequest request = StatisticsRequestBuilder.createStatisticsRequest().withAmount(amount).withTimestamp(timestamp).build();
            TransactionRequest request = new TransactionRequest();
            request.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            request.setAmount(amount);
            executorService.submit(() -> controller.createTransaction(request));
            n++;
            double newAmount =  amount.doubleValue();
            newAmount += 1;
            amount = BigDecimal.valueOf(newAmount);
            timestamp -= 60000;
        }

        executorService.shutdown();
        Thread.sleep(1000);
        ResponseEntity<?> response = controller.getTransactionStatics();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(count, ((StatisticsResponse) Objects.requireNonNull(response.getBody())).getCount());
    }

    private BigDecimal roundUpAmount(BigDecimal amount){
        BigDecimal newAmount = BigDecimal.valueOf(amount.doubleValue());
        return newAmount.setScale(2, RoundingMode.HALF_UP);
    }
}