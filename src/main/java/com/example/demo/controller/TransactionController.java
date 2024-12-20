package com.example.demo.controller;

import com.example.demo.module.Transaction;
import com.example.demo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET-метод для получения всех транзакций
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // GET-метод для получения транзакции по её id
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST-метод для добавления новой транзакции
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        if (transaction.getCategory() == null || transaction.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // если категория или пользователь не указаны
        }

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Double> getUserBalance(@PathVariable Long userId) {
        Double balance = transactionService.getUserBalance(userId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @RestController
    @RequestMapping("/analytics")
    public class AnalyticsController {


    }




}
