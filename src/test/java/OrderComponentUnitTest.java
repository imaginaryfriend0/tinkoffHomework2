import org.example.components.AccountComponent;
import org.example.components.OrderComponent;
import org.example.components.ProductComponent;
import org.example.components.UserComponent;
import org.example.entity.Account;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.enums.ProductType;
import org.example.repositories.AccountRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class OrderComponentUnitTest extends AbstractTest{
    @Mock
    UserComponent userComponent;

    @Mock
    ProductComponent productComponent;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    AccountComponent accountComponent;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    OrderComponent orderComponent;

    @Test
    void createOrderUnitTest() {
        var userName = "Oleg";
        var userPhone = "+79990001234";
        var productName = "Milk";
        var productPrice = 100;
        var balance = 100;

        var user = new User();
        user.setId(1L);
        user.setName(userName);
        user.setPhone(userPhone);

        var account = new Account();
        account.setUserId(user.getId());
        account.setBalance(100);

        var product = new Product();
        product.setId(1L);
        product.setName(productName);
        product.setPrice(productPrice);
        product.setProductType(ProductType.GOOD);
        product.setRemainder(1);

        Mockito.when(accountRepository.findByUserId(user.getId())).thenReturn(account);
        Mockito.when(userComponent.getOrCreateUser(userName, userPhone)).thenReturn(user);
        Mockito.when(accountComponent.createAccount(user.getId(),balance)).thenReturn(account);
        Mockito.when(productComponent.getProductByName(productName)).thenReturn(product);

        orderComponent.createOrder(userName, userPhone, productName);

        Mockito.verify(orderRepository, Mockito.times(1)).save(any());
    }

    @Test
    void createOrderUnitNegativeTest() {
        var userName = "Oleg";
        var userPhone = "+79990001234";
        var productName = "Milk";
        var productPrice = 100;

        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);

        Mockito.when(userComponent.getOrCreateUser(userName, userPhone)).thenThrow(new NullPointerException("asd"));

        assertThrows(NullPointerException.class, ()-> orderComponent.createOrder(userName, userPhone, productName));

        Mockito.verify(orderRepository, Mockito.times(0)).save(any());
    }
}
