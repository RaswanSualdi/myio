package com.myio.myincomeoutcome.dto.response;

import com.myio.myincomeoutcome.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private String description;
    private Long amount;
    private String transDate;
    private Category category;
}
