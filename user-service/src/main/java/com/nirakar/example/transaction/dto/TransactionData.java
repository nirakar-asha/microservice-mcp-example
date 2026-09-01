package com.nirakar.example.transaction.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionData {

    public static List<Transaction> getAllTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("TXN1001", LocalDate.of(2025, 9, 1), "Salary Credit", "Credit", 75000, 75000));
        transactions.add(new Transaction("TXN1002", LocalDate.of(2025, 9, 2), "Grocery Store", "Debit", 2350, 72650));
        transactions.add(new Transaction("TXN1003", LocalDate.of(2025, 9, 3), "Electricity Bill", "Debit", 1200, 71450));
        transactions.add(new Transaction("TXN1004", LocalDate.of(2025, 9, 4), "Mobile Recharge", "Debit", 399, 71051));
        transactions.add(new Transaction("TXN1005", LocalDate.of(2025, 9, 5), "Dining Out", "Debit", 1800, 69251));
        transactions.add(new Transaction("TXN1006", LocalDate.of(2025, 9, 6), "Freelance Payment", "Credit", 15000, 84251));
        transactions.add(new Transaction("TXN1007", LocalDate.of(2025, 9, 7), "Online Shopping", "Debit", 3499, 80752));
        transactions.add(new Transaction("TXN1008", LocalDate.of(2025, 9, 8), "Fuel Station", "Debit", 2000, 78752));
        transactions.add(new Transaction("TXN1009", LocalDate.of(2025, 9, 9), "Mutual Fund Redemption", "Credit", 10000, 88752));
        transactions.add(new Transaction("TXN1010", LocalDate.of(2025, 9, 10), "ATM Withdrawal", "Debit", 5000, 83752));
        transactions.add(new Transaction("TXN1011", LocalDate.of(2025, 9, 11), "Insurance Premium", "Debit", 6500, 77252));
        transactions.add(new Transaction("TXN1012", LocalDate.of(2025, 9, 12), "Interest Credit", "Credit", 750, 78002));
        transactions.add(new Transaction("TXN1013", LocalDate.of(2025, 9, 13), "Gym Membership", "Debit", 2000, 76002));
        transactions.add(new Transaction("TXN1014", LocalDate.of(2025, 9, 14), "Credit Card Payment", "Debit", 12000, 64002));
        transactions.add(new Transaction("TXN1015", LocalDate.of(2025, 9, 15), "Rent Transfer", "Debit", 25000, 39002));
        transactions.add(new Transaction("TXN1016", LocalDate.of(2025, 9, 15), "Cashback Credit", "Credit", 500, 39502));
        transactions.add(new Transaction("TXN1017", LocalDate.of(2025, 9, 15), "Book Purchase", "Debit", 1200, 38302));
        transactions.add(new Transaction("TXN1018", LocalDate.of(2025, 9, 15), "UPI Transfer", "Debit", 3000, 35302));
        transactions.add(new Transaction("TXN1019", LocalDate.of(2025, 9, 15), "Refund from Vendor", "Credit", 1500, 36802));
        transactions.add(new Transaction("TXN1020", LocalDate.of(2025, 9, 15), "Coffee Shop", "Debit", 250, 36552));

        return transactions;
    }


    public static List<Transaction> getLastNTransactions(List<Transaction> transactions, int n) {
        return transactions.stream()
                .sorted(Comparator.comparing((Transaction t) -> t.date()).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public static Transaction getTransactionById(String transactionId) {
        return getAllTransaction().stream()
                .filter(transaction -> transaction.transactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    public static List<Transaction> getTransactionsByUser(String userName) {
        List<UserTransactionMapping> userTransactionMappings = getUserTransactionMapping();
        return userTransactionMappings.stream()
                .filter(mapping -> mapping.getUser().name().equalsIgnoreCase(userName))
                .flatMap(mapping -> mapping.getTransactions().stream())
                .collect(Collectors.toList());
    }

    public static List<UserTransactionMapping> getUserTransactionMapping() {
        List<UserTransactionMapping> userTransactionMappings = new ArrayList<>();
        List<User> users = User.getAll();
        List<Transaction> transactions = getAllTransaction();

        userTransactionMappings.add(
                new UserTransactionMapping(
                        users.get(0),      // John Smith
                        transactions.subList(0, 4)
                ));

        userTransactionMappings.add(
                new UserTransactionMapping(
                        users.get(1),      // Jane Doe
                        transactions.subList(5, 10)
                ));

        userTransactionMappings.add(
                new UserTransactionMapping(
                        users.get(2),      // Michael Johnson
                        transactions.subList(10, 15)
                ));

        userTransactionMappings.add(
                new UserTransactionMapping(
                        users.get(3),      // Emily Williams
                        transactions.subList(15, 20)
                ));

        return userTransactionMappings;
    }


}

