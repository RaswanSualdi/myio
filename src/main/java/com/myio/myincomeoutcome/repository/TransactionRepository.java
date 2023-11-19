package com.myio.myincomeoutcome.repository;

import com.myio.myincomeoutcome.constant.Category;
import com.myio.myincomeoutcome.entity.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {
    @Query(value = "SELECT * FROM t_transaction WHERE DATE(transaction_date) = :date", nativeQuery = true)
    List<Transaction> findTransactionsByDate(@Param("date") LocalDate date);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.transDate BETWEEN :startDate AND :endDate AND t.category.name = :categoryName")
    Long getTotalAmountByCategoryAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryName")Category categoryName
            );

    @Query(value = "SELECT * FROM t_transaction", nativeQuery = true)
    List<Transaction> findAllTransactions();

    @Query("SELECT t FROM Transaction t WHERE t.category.name = :categoryName")
    List<Transaction> findAllByCategory(@Param("categoryName") Category categoryName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO t_transaction (id,description, amount, transaction_date, user_credential_id, category_id) " +
            "VALUES (:id,:description, :amount, :transDate, :userCredentialId, :categoryId)", nativeQuery = true)
    void saveTransaction(
            @Param("id") String id,
            @Param("description") String description,
            @Param("amount") Long amount,
            @Param("transDate") LocalDateTime transDate,
            @Param("userCredentialId") String userCredentialId,
            @Param("categoryId") String categoryId
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE from t_transaction WHERE id = :transactionId", nativeQuery = true)
    void deleteTransaction(@Param("transactionId") String transactionId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_transaction SET amount = :amount, description = :description," +
            " transaction_date = :transDate WHERE id = :transactionId", nativeQuery = true)
    void updateTransaction(@Param("transactionId") String transactionId,
                           @Param("amount") Long amount,
                           @Param("transDate") LocalDate transDate,
                           @Param("description") String description
    );


}
