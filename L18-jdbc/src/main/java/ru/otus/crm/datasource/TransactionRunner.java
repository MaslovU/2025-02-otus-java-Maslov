package ru.otus.crm.datasource;

public interface TransactionRunner {

    <T> T doInTransaction(TransactionAction<T> action);
}