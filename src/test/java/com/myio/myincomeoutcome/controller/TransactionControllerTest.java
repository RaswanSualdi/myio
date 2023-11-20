package com.myio.myincomeoutcome.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myio.myincomeoutcome.dto.request.TransactionRequest;
import com.myio.myincomeoutcome.dto.request.UpdateTransactionRequest;
import com.myio.myincomeoutcome.dto.response.CommonResponse;
import com.myio.myincomeoutcome.dto.response.TransactionResponse;
import com.myio.myincomeoutcome.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void createNewIncome_Success() throws Exception {

        TransactionRequest request = new TransactionRequest("userId", "Description", 100L);

        when(transactionService.createNewIncome(request)).thenReturn("Data berhasil ditambahkan");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        CommonResponse<Object> response = new ObjectMapper().readValue(content, CommonResponse.class);
        assertEquals("successfully create new income", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

        times(1);
    }

    @Test
    void createNewOutCome_Success() throws Exception {
        TransactionRequest request = new TransactionRequest("userId", "Description", 100L);

        when(transactionService.createNewOutcome(request)).thenReturn("Data berhasil ditambahkan");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions/outcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

            String content = result.getResponse().getContentAsString();
        CommonResponse<Object> response = new ObjectMapper().readValue(content, CommonResponse.class);
        assertEquals("successfully create new outcome", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
//        verify(transactionService, times(1)).createNewOutcome(request);
        times(1);

    }

    @Test
    void getAllTransactions_NoDate_Success() throws Exception {

        when(transactionService.findAllTransactions()).thenReturn(Collections.emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

            String content = result.getResponse().getContentAsString();
        CommonResponse<List<TransactionResponse>> response = new ObjectMapper().readValue(content, CommonResponse.class);
        assertEquals("successfully get all transactions", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getData());
    }

    @Test
    void getAllTransactions_WithDate_Success() throws Exception {
        LocalDate localDate = LocalDate.now();
        when(transactionService.getTransactionsByDate(localDate)).thenReturn(Collections.emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions")
                .param("date", localDate.toString())
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        CommonResponse<List<TransactionResponse>> response = new ObjectMapper().readValue(content, CommonResponse.class);
        assertEquals("successfully get all transactions", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getData());
    }

    @Test
    void deleteTransactionById_Success() throws Exception {

        String transactionId = "exampleTransactionId";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/transactions")
                .param("transactionId", transactionId);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        verify(transactionService, times(1)).deleteTransactionById(transactionId);

        String content = result.getResponse().getContentAsString();
        CommonResponse<Object> response = new ObjectMapper().readValue(content, CommonResponse.class);
        assertEquals("successfully delete transaction", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void updateTransactionById_Success() throws Exception {

        UpdateTransactionRequest request = new UpdateTransactionRequest();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        verify(transactionService, times(1)).updateTransactionById(request);

        String content = result.getResponse().getContentAsString();
        CommonResponse<Object> response = new ObjectMapper().readValue(content, CommonResponse.class);
        assertEquals("successfully update transaction", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void findTransactionByIncome_Success() throws Exception {

        List<TransactionResponse> mockTransactions = new ArrayList<>();

        when(transactionService.findAllTransactionByIncome()).thenReturn(mockTransactions);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/transactions/income");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        verify(transactionService, times(1)).findAllTransactionByIncome();

        String content = result.getResponse().getContentAsString();
        CommonResponse<List<TransactionResponse>> response = new ObjectMapper().readValue(content, new TypeReference<CommonResponse<List<TransactionResponse>>>() {});
        assertEquals("successfully get all transactions", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(mockTransactions, response.getData());
    }

    @Test
    void findTransactionByOutcome_Success() throws Exception {
        List<TransactionResponse> mockTransactions = new ArrayList<>();
        when(transactionService.findAllTransactionByOutcome()).thenReturn(mockTransactions);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/transactions/outcome");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        verify(transactionService, times(1)).findAllTransactionByOutcome();

        String content = result.getResponse().getContentAsString();
        CommonResponse<List<TransactionResponse>> response = new ObjectMapper().readValue(content, new TypeReference<CommonResponse<List<TransactionResponse>>>() {});
        assertEquals("successfully get all transactions", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(mockTransactions, response.getData());
    }

    @Test
    void getTotalIncomeByDate_Success() throws Exception {
        String startDate = "2023-11-19";
        String endDate = "2023-11-20";
        Long mockTotalIncome = 1000L;
        when(transactionService.getTotalIncomeByDate(any(LocalDate.class), any(LocalDate.class))).thenReturn(mockTotalIncome);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/transactions/total-income")
                .param("startDate", startDate)
                .param("endDate", endDate);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(transactionService, times(1)).getTotalIncomeByDate(LocalDate.parse(startDate), LocalDate.parse(endDate));

        String content = result.getResponse().getContentAsString();
        Long responseTotalIncome = new ObjectMapper().readValue(content, Long.class);
        assertEquals(mockTotalIncome, responseTotalIncome);
    }

    @Test
    void getTotalOutcomeByDate_Success() throws Exception {
        String startDate = "2023-11-19";
        String endDate = "2023-11-20";
        Long mockTotalOutcome = 800L;
        when(transactionService.getTotalOutcomeByDate(any(LocalDate.class), any(LocalDate.class))).thenReturn(mockTotalOutcome);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/transactions/total-outcome")
                .param("startDate", startDate)
                .param("endDate", endDate);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(transactionService, times(1)).getTotalOutcomeByDate(LocalDate.parse(startDate), LocalDate.parse(endDate));

        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Long responseTotalOutcome = objectMapper.readValue(content, Long.class);
        assertEquals(mockTotalOutcome, responseTotalOutcome);
    }
}