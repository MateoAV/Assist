package com.example.accounts.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BankAccount{
    private int accountNumber;
    private double balance;
}