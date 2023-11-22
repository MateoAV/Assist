package com.example.accounts.repository;

import com.example.accounts.model.BankAccount;

import java.util.Optional;

public interface BankAccountRepository {
    Optional<BankAccount> findById(String accountId);

    void save(BankAccount account);
}