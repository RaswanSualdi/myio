package com.myio.myincomeoutcome.service.impl;


import com.myio.myincomeoutcome.dto.request.TransactionRequest;
import com.myio.myincomeoutcome.dto.request.UpdateTransactionRequest;
import com.myio.myincomeoutcome.dto.response.TransactionResponse;
import com.myio.myincomeoutcome.entity.AppUser;
import com.myio.myincomeoutcome.entity.Category;
import com.myio.myincomeoutcome.entity.Transaction;
import com.myio.myincomeoutcome.entity.UserCredential;
import com.myio.myincomeoutcome.repository.TransactionRepository;
import com.myio.myincomeoutcome.repository.UserCredentialRepository;
import com.myio.myincomeoutcome.service.CategoryService;
import com.myio.myincomeoutcome.service.TransactionService;
import com.myio.myincomeoutcome.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final UserCredentialRepository userCredentialRepository;
    private final CategoryService categoryService;
    @Transactional(rollbackFor = Exception.class)
    @Override
   public Object createNewIncome(TransactionRequest request){
       try{
           validationUtil.validate(request);
           Category category = categoryService.getOrSave(Category.builder().name(com.myio.myincomeoutcome.constant.Category.INCOME).build());
           UserCredential currentUser = userCredentialRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           AppUser appUser = (AppUser) authentication.getPrincipal();
           if (!appUser.getId().equals(currentUser.getId()))
               throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
            transactionRepository.saveTransaction( UUID.randomUUID().toString(),request.getDescription(), request.getAmount(), LocalDateTime.now(), currentUser.getId(), category.getId());
            return "Data berhasil ditambahkan";

       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object createNewOutcome(TransactionRequest request){
        try{
            validationUtil.validate(request);
            Category category = categoryService.getOrSave(Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).build());
            UserCredential currentUser = userCredentialRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUser appUser = (AppUser) authentication.getPrincipal();
            if (!appUser.getId().equals(currentUser.getId()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
            transactionRepository.saveTransaction( UUID.randomUUID().toString(),request.getDescription(), request.getAmount(), LocalDateTime.now(), currentUser.getId(), category.getId());
            return "Data berhasil ditambahkan";

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deleteTransactionById(String id) {
        try {
            transactionRepository.deleteTransaction(id);
            return "Successfully delete transaction";
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object updateTransactionById(UpdateTransactionRequest request) {
        try{
            transactionRepository.updateTransaction(request.getId(), request.getAmount(), request.getTransDate(),request.getDescription());
            return "successfully update transaction";
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<TransactionResponse> findAllTransactions() {

            List<Transaction> transactions = transactionRepository.findAllTransactions();
            List<TransactionResponse> transactionResponses = transactions.stream().map(transaction -> TransactionResponse.builder()
                    .id(transaction.getId())
                    .amount(transaction.getAmount())
                    .description(transaction.getDescription())
                    .transDate(transaction.getTransDate().toString())
                    .category(transaction.getCategory())
                    .build()
            ).collect(Collectors.toList());
            return transactionResponses;

    }

    @Override
    public List<TransactionResponse> findAllTransactionByIncome() {
        List<Transaction> transactions = transactionRepository.findAllByCategory(com.myio.myincomeoutcome.constant.Category.INCOME);
        List<TransactionResponse> transactionResponses = transactions.stream().map(transaction -> TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transDate(transaction.getTransDate().toString())
                .category(transaction.getCategory())
                .build()
        ).collect(Collectors.toList());
        return transactionResponses;
    }

    @Override
    public List<TransactionResponse> findAllTransactionByOutcome() {
        List<Transaction> transactions = transactionRepository.findAllByCategory(com.myio.myincomeoutcome.constant.Category.OUTCOME);
        List<TransactionResponse> transactionResponses = transactions.stream().map(transaction -> TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transDate(transaction.getTransDate().toString())
                .category(transaction.getCategory())
                .build()
        ).collect(Collectors.toList());
        return transactionResponses;
    }

    public List<TransactionResponse> getTransactionsByDate(LocalDate date) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDate(date);
        List<TransactionResponse> transactionResponses = transactions.stream().map(transaction -> TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transDate(transaction.getTransDate().toString())
                .category(transaction.getCategory())
                .build()
        ).collect(Collectors.toList());
        return transactionResponses;
    }

    public Long getTotalIncomeByDate(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getTotalAmountByCategoryAndDate(startDate, endDate, com.myio.myincomeoutcome.constant.Category.INCOME);
    }

    public Long getTotalOutcomeByDate(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getTotalAmountByCategoryAndDate(startDate, endDate, com.myio.myincomeoutcome.constant.Category.OUTCOME);
    }


}
