import org.example.components.OrderComponent;
import org.example.components.ProductComponent;
import org.example.components.UserComponent;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderComponentTest extends AbstractTest {
    @Autowired
    UserComponent userComponent;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductComponent productComponent;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderComponent orderComponent;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({
            "Oleg, +7999", "Petya, +7888"
        }
    )
    void createOrderTest(String userName, String userPhone) {
        // AS IS
        // var userName = "Oleg";
        System.out.println("USERNAME = " + userName);
       // var userPhone = "+79990001234";
        var productName = "Milk";
        var productPrice = 100;

        var product = productComponent.addNewGoods(productName, productPrice);
        // TEST
        var createdOrder = orderComponent.createOrder(userName, userPhone, productName);

        // ASSERTS
        var order = orderRepository.findById(createdOrder.getId()).get();

        var authorId = order.getAuthorId();

        var user = userRepository.findById(authorId);
        assertThat(user).isNotEmpty();
        assertThat(user.get().getPhone()).isEqualTo(userPhone);
        assertThat(user.get().getName()).isEqualTo(userName);

        var productId = order.getProductId();
        assertThat(product.getId()).isEqualTo(productId);
    }

    @Test
    void errorWhenTryToCreateOrderWithoutProductTest() {
        var productName = "Beer";
        var error = assertThrows(
                NoSuchElementException.class,
                () -> orderComponent.createOrder("Maxim", "12345", productName)
        );

        assertThat(error.getMessage()).isEqualTo("Продукта с именем '%s' нет!", productName);
    }
}
