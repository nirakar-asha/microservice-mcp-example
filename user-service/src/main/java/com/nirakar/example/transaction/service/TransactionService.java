package com.nirakar.example.transaction.service;

import com.nirakar.example.transaction.dto.Transaction;
import com.nirakar.example.transaction.dto.TransactionData;
import dev.toonformat.jtoon.JToon;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class TransactionService {

    @Tool(name="fetch_n_transaction",description = "fetch last n transaction details for a user. Use this tool if a user wants to fetch last n transaction details")
    public String fetchNTransactionTool(int number) {
        log.info("Tool calling :: fetch_n_transaction.... {}", number);
        var lastNTransactions = fetchTransaction(number);
        String jtoonTransactions = JToon.encode(lastNTransactions);
        log.info("Tool calling finished :: fetch_n_transaction: {}", jtoonTransactions);
        return jtoonTransactions;
    }

    @Tool(name="fetch_transaction_by_id",description = "fetch transaction details by transaction id. Use this tool if a user has transaction id and wants to fetch the transaction details")
    public String fetchTransactionByIdTool(String transactionId) {
        log.info("Tool calling :: fetch_transaction_by_id.... {}", transactionId);
        var transaction = TransactionData.getTransactionById(transactionId);
        String jtoonTransaction = JToon.encode(transaction);
        log.info("Tool calling finished :: fetch_transaction_by_id: {}", jtoonTransaction);
        return jtoonTransaction;
    }

    @Tool(name="fetch_transaction_by_user",description = "fetch transaction details by user name. Use this tool if a user wants to fetch all transaction details for a user")
    public String fetchTransactionByUserTool(String userName) {
        log.info("Tool calling :: fetch_transaction_by_user.... {}", userName);
        var transactions = TransactionData.getTransactionsByUser(userName);
        String jtoonTransactions = JToon.encode(transactions);
        log.info("Tool calling finished :: fetch_transaction_by_user: {}", jtoonTransactions);
        return jtoonTransactions;
    }

    public List<Transaction> fetchTransaction(int number) {
        log.info("Fetching transaction service.... {}", number);
        List<Transaction> allTransaction = TransactionData.getAllTransaction();
        return TransactionData.getLastNTransactions(allTransaction, number);
    }


}
