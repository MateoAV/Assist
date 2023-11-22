package com.example.accounts.controller;

import com.example.accounts.model.BankAccount;
import com.example.accounts.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class BankAccountController {

    @Autowired
    private BankAccountRepository repository;

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<BankAccount> deposit(@PathVariable String accountId, @RequestBody double amount) {
        BankAccount account = repository.findById(accountId).orElse(new BankAccount());
        account.setBalance(account.getBalance() + amount);
        repository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<BankAccount> withdraw(@PathVariable String accountId, @RequestBody double amount) throws InsufficientFundsException {
        BankAccount account = repository.findById(accountId).orElseThrow(RuntimeException::new);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        repository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Exception handler
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private class InsufficientFundsException extends Throwable {
        public InsufficientFundsException(String insufficient_funds) {
        }
    }
}