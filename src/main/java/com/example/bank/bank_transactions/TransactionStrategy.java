package com.example.bank.bank_transactions;

import java.util.concurrent.Callable;

public interface TransactionStrategy<T> {

    public Boolean createTransaction(T transactionData);

}
