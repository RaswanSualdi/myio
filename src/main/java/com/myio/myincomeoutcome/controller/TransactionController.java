package com.myio.myincomeoutcome.controller;


import com.myio.myincomeoutcome.dto.request.TransactionRequest;
import com.myio.myincomeoutcome.dto.request.UpdateTransactionRequest;
import com.myio.myincomeoutcome.dto.response.CommonResponse;
import com.myio.myincomeoutcome.dto.response.TransactionResponse;
import com.myio.myincomeoutcome.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/income")
    public ResponseEntity<?> createNewIncome(@RequestBody TransactionRequest request){
        transactionService.createNewIncome(request);
        CommonResponse<Object> response = CommonResponse.<Object>builder()
                .message("successfully create new income")
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @PostMapping("/outcome")
    public ResponseEntity<?> createNewOutCome(@RequestBody TransactionRequest request){
        transactionService.createNewOutcome(request);
        CommonResponse<Object> response = CommonResponse.<Object>builder()
                .message("successfully create new outcome")
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions(@RequestParam(required = false) String date){
        if(date==null){
            List<TransactionResponse> transactions = transactionService.findAllTransactions();
            CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                    .message("successfully get all transactions")
                    .statusCode(HttpStatus.CREATED.value())
                    .data(transactions)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }
        LocalDate localDate = LocalDate.parse(date);
        List<TransactionResponse> transactions = transactionService.getTransactionsByDate(localDate);
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .message("successfully get all transactions")
                .statusCode(HttpStatus.CREATED.value())
                .data(transactions)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteTransactionById(@RequestParam String transactionId){
        transactionService.deleteTransactionById(transactionId);
        CommonResponse<Object> response = CommonResponse.<Object>builder()
                .message("successfully delete transaction")
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTransactionById(@RequestBody UpdateTransactionRequest request){
        transactionService.updateTransactionById(request);
        CommonResponse<Object> response = CommonResponse.<Object>builder()
                .message("successfully update transaction")
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/income")
    public ResponseEntity<?> findTransactionByIncome(){
        List<TransactionResponse> transactions = transactionService.findAllTransactionByIncome();
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .message("successfully get all transactions")
                .statusCode(HttpStatus.CREATED.value())
                .data(transactions)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/outcome")
    public ResponseEntity<?> findTransactionByOutcome(){
        List<TransactionResponse> transactions = transactionService.findAllTransactionByOutcome();
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .message("successfully get all transactions")
                .statusCode(HttpStatus.CREATED.value())
                .data(transactions)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/total-income")
    public ResponseEntity<Long> getTotalIncomeByDate(@RequestParam String startDate,@RequestParam String endDate ) {
        LocalDate parseStartDate = LocalDate.parse(startDate);
        LocalDate parseEndDate = LocalDate.parse(endDate);
        Long totalIncome = transactionService.getTotalIncomeByDate(parseStartDate, parseEndDate);
        return ResponseEntity.ok(totalIncome);
    }

    @GetMapping("/total-outcome")
    public ResponseEntity<Long> getTotalOutcomeByDate(@RequestParam String startDate,@RequestParam String endDate ) {
        LocalDate parseStartDate = LocalDate.parse(startDate);
        LocalDate parseEndDate = LocalDate.parse(endDate);
        Long totalOutcome = transactionService.getTotalOutcomeByDate(parseStartDate, parseEndDate);
        return ResponseEntity.ok(totalOutcome);
    }
}
