package org.example.components;

import org.example.entity.Account;
import org.example.entity.User;
import org.example.repositories.AccountRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class UserComponent {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    public User getOrCreateUser(String name, String phone) {
       var isUserFound = userRepository.findByPhone(phone);

       if(isUserFound != null){
           var isAccountFound = accountRepository.findByUserId(isUserFound.getId());
           if(isAccountFound != null) {
               return isUserFound;
           }
           Account account = new Account();
           account.setUserId(isUserFound.getId());
           account.setBalance(100);

           accountRepository.save(account);

           return isUserFound;
       }
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        userRepository.save(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setBalance(100);
        accountRepository.save(account);

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByPhone(String phone) {
        var user = userRepository.findByPhone(phone);
        if (user != null) {
            return user;
        }

        throw new NoSuchElementException(
                String.format(
                        "Пользователя с телефоном '%s' не существует!", phone));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);

    }
}
