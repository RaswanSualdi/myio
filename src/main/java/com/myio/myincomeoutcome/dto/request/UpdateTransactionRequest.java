package com.myio.myincomeoutcome.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTransactionRequest {
    private String id;
    private LocalDate transDate;
    private String description;
    private Long amount;
}
