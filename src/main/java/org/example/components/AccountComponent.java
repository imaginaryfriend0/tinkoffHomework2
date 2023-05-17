package org.example.components;

import org.example.entity.Account;
import org.example.repositories.AccountRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class AccountComponent {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    public List<Account> getListOfAccounts() {
        return accountRepository.findAll();
    }

    public Account findAccountByUserId (Long userId){
        var account = accountRepository.findByUserId(userId);
        if (account!=null){
            return account;
        }
        throw new NoSuchElementException(
                String.format("Аккаунта пользователя с Id '%s' не существует!", userId));
    }

    public Account addBalanceByPhoneNumber(String phone, long balance) {
        var user = userRepository.findByPhone(phone);
        if(user!=null) {
            var account = findAccountByUserId(user.getId());
            if (account != null) {
                account.setBalance(account.getBalance() + balance);
                accountRepository.save(account);
                return account;
            }
        }throw new NoSuchElementException(
                String.format("Пользователя с телефоном '%s' не существует!", phone));
    }

    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
    }


    public Account createAccount(long userId, long balance) {
        var account = accountRepository.findByUserId(userId);
        if(account==null){
            if(balance>=0) {
                Account accountNew = new Account(userId, balance);
                accountRepository.save(accountNew);
                return accountNew;
            } throw new NoSuchElementException(
                String.format("Баланс не может принимать отрицательные значения вроде '%s'",balance)
            );
        }return account;
    }

}
