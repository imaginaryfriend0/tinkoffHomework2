package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.example.components.AccountComponent;
import org.example.entity.Account;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class AccountController {
    @Autowired
    AccountComponent accountComponent;

    @PutMapping("addBalanceByPhoneNumber")
    @Operation(summary = "Пополнение баланса пользователя по номеру телефона")
    public Account addBalance(
            @RequestParam String phone,
            @RequestParam long balance) {
        return accountComponent.addBalanceByPhoneNumber(phone,balance);
    }

    @PostMapping("createAccount")
    @Operation(summary = "Создание нового аккаунта")
    public Account createAccount(
            @RequestParam long id,
            @RequestParam long userBalance) {
        return accountComponent.createAccount(id,userBalance);
    }

    @GetMapping("allAccounts")
    @Operation(summary = "Найти все аккаунты")
    public List<Account> allAccounts(){
        return accountComponent.getListOfAccounts();
    }


    @DeleteMapping("deleteAccountById")
    @Operation(summary = "Удаление аккаунта")
    public void deleteAccountById(@RequestParam Long id) {
        accountComponent.deleteAccountById(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> handleException(NoSuchElementException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

}
