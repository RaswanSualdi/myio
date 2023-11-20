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
import com.myio.myincomeoutcome.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TransactionServiceImplTest {

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void createNewIncome_Unauthorized() {
        TransactionRequest request = new TransactionRequest("userId", "Income description", 100L);
        Category incomeCategory = Category.builder().name(com.myio.myincomeoutcome.constant.Category.INCOME).build();
        UserCredential currentUser = new UserCredential();
        Authentication authentication = Mockito.mock(Authentication.class);
        AppUser appUser = new AppUser();
        appUser.setId("anotherUserId");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(userCredentialRepository.findById("userId")).thenReturn(java.util.Optional.of(currentUser));
        when(categoryService.getOrSave(any(Category.class))).thenReturn(incomeCategory);
        doNothing().when(validationUtil).validate(request);

        try {
            transactionService.createNewIncome(request);
        } catch (RuntimeException e) {
            assertEquals("org.springframework.web.server.ResponseStatusException: 401 UNAUTHORIZED \"unauthorized\"", e.getMessage());
            verify(validationUtil, times(1)).validate(request);
            verify(userCredentialRepository, times(1)).findById("userId");
            verify(categoryService, times(1)).getOrSave(any(Category.class));
            verify(transactionRepository, never()).saveTransaction(anyString(), anyString(), anyLong(), any(LocalDateTime.class), anyString(), anyString());
        }
    }

    @Test
    void createNewOutcome_Success() {
        TransactionRequest request = new TransactionRequest("userId", "Outcome description", 100L);
        Category outcomeCategory = Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).build();
        UserCredential currentUser = new UserCredential();
        Authentication authentication = Mockito.mock(Authentication.class);
        AppUser appUser = new AppUser();
        appUser.setId("userId");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(userCredentialRepository.findById("userId")).thenReturn(java.util.Optional.of(currentUser));
        when(categoryService.getOrSave(any(Category.class))).thenReturn(outcomeCategory);
        doNothing().when(validationUtil).validate(request);

        Object result = transactionService.createNewOutcome(request);

        assertEquals("Data berhasil ditambahkan", result);
        verify(validationUtil, times(1)).validate(request);
        verify(userCredentialRepository, times(1)).findById("userId");
        verify(categoryService, times(1)).getOrSave(any(Category.class));
        verify(transactionRepository, times(1)).saveTransaction(anyString(), anyString(), anyLong(), any(LocalDateTime.class), anyString(), anyString());
    }

    @Test
    void createNewOutcome_Unauthorized() {
        TransactionRequest request = new TransactionRequest("userId", "Outcome description", 100L);
        Category outcomeCategory = Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).build();
        UserCredential currentUser = new UserCredential();
        Authentication authentication = Mockito.mock(Authentication.class);
        AppUser appUser = new AppUser();
        appUser.setId("anotherUserId");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(userCredentialRepository.findById("userId")).thenReturn(java.util.Optional.of(currentUser));
        when(categoryService.getOrSave(any(Category.class))).thenReturn(outcomeCategory);
        doNothing().when(validationUtil).validate(request);

        assertThrows(RuntimeException.class, () -> {
            transactionService.createNewOutcome(request);
        });
        verify(validationUtil, times(1)).validate(request);
        verify(userCredentialRepository, times(1)).findById("userId");
        verify(categoryService, times(1)).getOrSave(any(Category.class));
        verify(transactionRepository, never()).saveTransaction(anyString(), anyString(), anyLong(), any(LocalDateTime.class), anyString(), anyString());
    }

    @Test
    void deleteTransactionById_Success() {
        String transactionId = "123";
        Object result = transactionService.deleteTransactionById(transactionId);

        assertEquals("Successfully delete transaction", result);
        verify(transactionRepository, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void deleteTransactionById_Failure() {
        String transactionId = "123";
        doThrow(new RuntimeException("Simulated error")).when(transactionRepository).deleteTransaction(anyString());

        assertThrows(RuntimeException.class, () -> {
            transactionService.deleteTransactionById(transactionId);
        });
        verify(transactionRepository, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void updateTransactionById_Success() {
        UpdateTransactionRequest updateRequest = new UpdateTransactionRequest("123", LocalDate.now(), "Updated description", 10L);

        Object result = transactionService.updateTransactionById(updateRequest);

        assertEquals("successfully update transaction", result);
        verify(transactionRepository, times(1)).updateTransaction(updateRequest.getId(), updateRequest.getAmount(), updateRequest.getTransDate(), updateRequest.getDescription());
    }

    @Test
    void updateTransactionById_Failure() {
        UpdateTransactionRequest updateRequest = new UpdateTransactionRequest("123", LocalDate.now(), "Updated description", 10L);
        doThrow(new RuntimeException("Simulated error")).when(transactionRepository).updateTransaction(anyString(), anyLong(), any(LocalDate.class), anyString());

        assertThrows(RuntimeException.class, () -> {
            transactionService.updateTransactionById(updateRequest);
        });
        verify(transactionRepository, times(1)).updateTransaction(updateRequest.getId(), updateRequest.getAmount(), updateRequest.getTransDate(), updateRequest.getDescription());
    }

    @Test
    void findAllTransactions_Success() {
        Category category1 = Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).id("1").build();
        Category category2 = Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).id("2").build();

        Transaction transaction1 = Transaction.builder()
                .id("1")
                .description("Description 1")
                .amount(100L)
                .transDate(LocalDate.now())
                .category(category1)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id("2")
                .description("Description 2")
                .amount(200L)
                .transDate(LocalDate.now())
                .category(category2)
                .build();

        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAllTransactions()).thenReturn(mockTransactions);

        List<TransactionResponse> result = transactionService.findAllTransactions();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals(100L, result.get(0).getAmount());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(LocalDate.now().toString(), result.get(0).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.OUTCOME, result.get(0).getCategory().getName());

        assertEquals("2", result.get(1).getId());
        assertEquals(200L, result.get(1).getAmount());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals(LocalDate.now().toString(), result.get(1).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.OUTCOME, result.get(1).getCategory().getName());

        verify(transactionRepository, times(1)).findAllTransactions();
    }

    @Test
    void findAllTransactions_EmptyList() {
        when(transactionRepository.findAllTransactions()).thenReturn(Arrays.asList());

        List<TransactionResponse> result = transactionService.findAllTransactions();

        assertEquals(0, result.size());

        verify(transactionRepository, times(1)).findAllTransactions();
    }

    @Test
    void findAllTransactionByIncome_Success() {
        Category incomeCategory = Category.builder().name(com.myio.myincomeoutcome.constant.Category.INCOME).id("1").build();

        Transaction transaction1 = Transaction.builder()
                .id("1")
                .description("Description 1")
                .amount(100L)
                .transDate(LocalDate.now())
                .category(incomeCategory)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id("2")
                .description("Description 2")
                .amount(200L)
                .transDate(LocalDate.now())
                .category(incomeCategory)
                .build();

        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAllByCategory(com.myio.myincomeoutcome.constant.Category.INCOME)).thenReturn(mockTransactions);

        List<TransactionResponse> result = transactionService.findAllTransactionByIncome();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals(100L, result.get(0).getAmount());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(LocalDate.now().toString(), result.get(0).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.INCOME, result.get(0).getCategory().getName());

        assertEquals("2", result.get(1).getId());
        assertEquals(200L, result.get(1).getAmount());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals(LocalDate.now().toString(), result.get(1).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.INCOME, result.get(1).getCategory().getName());

        verify(transactionRepository, times(1)).findAllByCategory(com.myio.myincomeoutcome.constant.Category.INCOME);
    }

    @Test
    void findAllTransactionByOutcome_Success() {
        Category outcomeCategory = Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).id("1").build();

        Transaction transaction1 = Transaction.builder()
                .id("1")
                .description("Description 1")
                .amount(100L)
                .transDate(LocalDate.now())
                .category(outcomeCategory)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id("2")
                .description("Description 2")
                .amount(200L)
                .transDate(LocalDate.now())
                .category(outcomeCategory)
                .build();

        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAllByCategory(com.myio.myincomeoutcome.constant.Category.OUTCOME)).thenReturn(mockTransactions);

        List<TransactionResponse> result = transactionService.findAllTransactionByOutcome();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals(100L, result.get(0).getAmount());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(LocalDate.now().toString(), result.get(0).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.OUTCOME, result.get(0).getCategory().getName());

        assertEquals("2", result.get(1).getId());
        assertEquals(200L, result.get(1).getAmount());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals(LocalDate.now().toString(), result.get(1).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.OUTCOME, result.get(1).getCategory().getName());

        verify(transactionRepository, times(1)).findAllByCategory(com.myio.myincomeoutcome.constant.Category.OUTCOME);
    }

    @Test
    void getTransactionsByDate_Success() {
        LocalDate date = LocalDate.now();
        Category category = Category.builder().name(com.myio.myincomeoutcome.constant.Category.OUTCOME).id("1").build();

        Transaction transaction1 = Transaction.builder()
                .id("1")
                .description("Description 1")
                .amount(100L)
                .transDate(date)
                .category(category)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id("2")
                .description("Description 2")
                .amount(200L)
                .transDate(date)
                .category(category)
                .build();

        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findTransactionsByDate(date)).thenReturn(mockTransactions);

        List<TransactionResponse> result = transactionService.getTransactionsByDate(date);

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals(100L, result.get(0).getAmount());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(date.toString(), result.get(0).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.OUTCOME, result.get(0).getCategory().getName());

        assertEquals("2", result.get(1).getId());
        assertEquals(200L, result.get(1).getAmount());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals(date.toString(), result.get(1).getTransDate());
        assertEquals(com.myio.myincomeoutcome.constant.Category.OUTCOME, result.get(1).getCategory().getName());

        verify(transactionRepository, times(1)).findTransactionsByDate(date);
    }

    @Test
    void getTotalIncomeByDate_Success() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(transactionRepository.getTotalAmountByCategoryAndDate(startDate, endDate, com.myio.myincomeoutcome.constant.Category.INCOME)).thenReturn(500L);

        Long result = transactionService.getTotalIncomeByDate(startDate, endDate);

        assertEquals(500L, result);
        verify(transactionRepository, times(1)).getTotalAmountByCategoryAndDate(startDate, endDate, com.myio.myincomeoutcome.constant.Category.INCOME);
    }

    @Test
    void getTotalOutcomeByDate_Success() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(transactionRepository.getTotalAmountByCategoryAndDate(startDate, endDate, com.myio.myincomeoutcome.constant.Category.OUTCOME)).thenReturn(300L);

        Long result = transactionService.getTotalOutcomeByDate(startDate, endDate);

        assertEquals(300L, result);
        verify(transactionRepository, times(1)).getTotalAmountByCategoryAndDate(startDate, endDate, com.myio.myincomeoutcome.constant.Category.OUTCOME);
    }

}