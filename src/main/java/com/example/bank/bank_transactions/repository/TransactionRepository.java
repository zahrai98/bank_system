package com.example.bank.bank_transactions.repository;

import com.example.bank.bank_transactions.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "select t from TransactionEntity t WHERE t.account.id = :accountId order by t.createdAt desc")
    List<TransactionEntity> getAllByAccountId(Long accountId, Pageable pageable);
}
