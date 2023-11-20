package com.myio.myincomeoutcome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myio.myincomeoutcome.dto.request.TransactionRequest;
import com.myio.myincomeoutcome.dto.response.CommonResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
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
    void createNewOutCome() {
    }

    @Test
    void getAllTransactions() {
    }

    @Test
    void deleteTransactionById() {
    }

    @Test
    void updateTransactionById() {
    }

    @Test
    void findTransactionByIncome() {
    }

    @Test
    void findTransactionByOutcome() {
    }

    @Test
    void getTotalIncomeByDate() {
    }

    @Test
    void getTotalOutcomeByDate() {
    }
}