import org.example.components.AccountComponent;
import org.example.components.UserComponent;
import org.example.repositories.AccountRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountComponentTest extends AbstractTest{
    @Autowired
    UserComponent userComponent;

    @Autowired
    AccountComponent accountComponent;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({
            "aboltus, +151515, 100", "amogus, +222222, 100"
    }
    )
    void createAccountTest(String username,String userPhone, long balance) {
        // AS IS
        var user = userComponent.getOrCreateUser(username,userPhone);
        var userId = user.getId();
        var findaccount = accountRepository.findByUserId(userId);

        // TEST

        // ASSERTS

        assertThat(user).isNotNull();
        assertThat(findaccount).isNotNull();
        assertThat(findaccount.getBalance()).isEqualTo(balance);
        assertThat(findaccount.getUserId()).isEqualTo(userId);
        assertThat(findaccount.getBalance()).isGreaterThan(0);
        assertThat(user.getPhone()).isEqualTo(userPhone);
        assertThat(user.getName()).isEqualTo(username);
        assertThat(userId).isEqualTo(user.getId());

    }

    @Test
    void creatingNegativeBalanceAccountError() {
        var balance = -100;
        var userid = 1;
        var error = assertThrows(
                NoSuchElementException.class,
                () -> accountComponent.createAccount(userid,balance)
        );
        assertThat(error.getMessage()).isEqualTo("Баланс не может принимать отрицательные значения вроде '%s'",balance);
    }
}
