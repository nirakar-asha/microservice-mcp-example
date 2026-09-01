package com.nirakar.example.transaction.dto;

import java.util.List;


public class UserTransactionMapping {

    private User user;
    private List<Transaction> transactions;

    public UserTransactionMapping(User user, List<Transaction> transactions) {
        this.user = user;
        this.transactions = transactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "UserTransactionMapping{" +
                "user=" + user +
                ", transactions=" + transactions +
                '}';
    }
}
