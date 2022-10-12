package com.seerbit.assessment.service;

import com.seerbit.assessment.dto.request.TransactionRequest;
import com.seerbit.assessment.dto.response.StatisticsResponse;

public interface TransactionService {

    boolean createTransaction(TransactionRequest transactionRequest, long timestamp);
    StatisticsResponse getTransaction(long timestamp);
    void deleteTransaction();
}
