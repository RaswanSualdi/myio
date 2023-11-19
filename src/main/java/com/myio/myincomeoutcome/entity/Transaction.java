package com.myio.myincomeoutcome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String description;
    private Long amount;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(name = "transaction_date")
    private LocalDate transDate;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
