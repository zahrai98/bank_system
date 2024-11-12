package com.example.bank.user.repository;

import com.example.bank.user.model.BankAccountEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {


    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000")})
    @Query(value = "select b from BankAccountEntity b where b.id = :accountId")
    BankAccountEntity findAccountByIdWithLock(Long accountId);


    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000")})
    @Query(value = "select b from BankAccountEntity b where b.id = :accountId1 or b.id = :accountId2")
    List<BankAccountEntity> findTwoAccountByIdWithLock(Long accountId1, Long accountId2);

    List<BankAccountEntity> getByUserId(Long userId);

}
