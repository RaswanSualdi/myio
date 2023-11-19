package com.myio.myincomeoutcome.service;


import com.myio.myincomeoutcome.dto.request.TransactionRequest;
import com.myio.myincomeoutcome.dto.request.UpdateTransactionRequest;
import com.myio.myincomeoutcome.dto.response.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
        Object createNewIncome(TransactionRequest request);
        Object createNewOutcome(TransactionRequest request);
        Object deleteTransactionById(String id);
        Object updateTransactionById(UpdateTransactionRequest request);
        List<TransactionResponse> findAllTransactions();
        List<TransactionResponse> findAllTransactionByIncome();
        List<TransactionResponse> findAllTransactionByOutcome();
        List<TransactionResponse> getTransactionsByDate(LocalDate date);
        Long getTotalIncomeByDate(LocalDate startDate, LocalDate endDate);
        Long getTotalOutcomeByDate(LocalDate startDate, LocalDate endDate);



}
