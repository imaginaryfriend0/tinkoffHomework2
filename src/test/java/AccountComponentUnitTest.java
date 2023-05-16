import org.example.components.AccountComponent;
import org.example.components.UserComponent;
import org.example.entity.User;
import org.example.repositories.AccountRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class AccountComponentUnitTest {
    @Mock
    UserComponent userComponent;

    @Mock
    UserRepository userRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountComponent accountComponent;

    @Test
    void createAccountUnitTest() {
        var userName = "Oleg";
        var userPhone = "+79990001234";
        var balance = 100;

        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(250L);

        Mockito.when(userComponent.getOrCreateUser(userName, userPhone)).thenReturn(user);

        accountComponent.createAccount(user.getId(),balance);

        Mockito.verify(accountRepository, Mockito.times(1)).save(any());
    }

    @Test
    void createAccountNegativeUnitTest() {
        var userName = "Oleg";
        var userPhone = "+79990001234";
        var balance = -100;

        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);

        Mockito.when(accountComponent.createAccount(user.getId(),balance)).thenThrow(new NullPointerException("asd"));

        assertThrows(NullPointerException.class, ()-> accountComponent.createAccount(user.getId(),balance));

        Mockito.verify(accountRepository, Mockito.times(1)).save(any());
    }
}
