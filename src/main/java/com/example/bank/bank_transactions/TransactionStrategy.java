package com.example.bank.bank_transactions;

import java.util.concurrent.Callable;

public interface TransactionStrategy<T> {

    public void createTransaction(T transactionData);

}
