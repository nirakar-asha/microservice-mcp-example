package com.nirakar.example.transaction.controller;

import com.nirakar.example.transaction.dto.Transaction;
import com.nirakar.example.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// http://localhost:8072/myapp/user/fetch/2

@RestController
public class UserController {

    private final TransactionService transactionService;

    public UserController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/fetch/{numberOfTransactions}")
    public List<Transaction> fetchTransactions(@PathVariable Integer numberOfTransactions) {
        return transactionService.fetchTransaction(numberOfTransactions);
    }
}
