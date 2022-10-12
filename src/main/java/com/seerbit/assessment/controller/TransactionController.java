package com.seerbit.assessment.controller;


import com.seerbit.assessment.dto.request.TransactionRequest;
import com.seerbit.assessment.exception.CustomException;
import com.seerbit.assessment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;

@RestController
@CrossOrigin
@Validated
public class TransactionController {


    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest request) {
        long currentTime = Instant.now().toEpochMilli();
        try{
            boolean check = transactionService.createTransaction(request,currentTime);
            if(check){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }catch (Exception ex){
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/statistics")
    public ResponseEntity<?> getTransactionStatics() {
        long currentTime = Instant.now().toEpochMilli();
        return new ResponseEntity<>(transactionService.getTransaction(currentTime), HttpStatus.OK);
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<?> deleteTransaction() {
        transactionService.deleteTransaction();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
