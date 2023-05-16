import org.example.components.UserComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SuccessTest {
    //добавление товара
    //добавление заказа
    //проверка, что заказ добавлен
    // проверка , что пользватель и продукт в заказе корректный
    //удаление
@Autowired
UserComponent userComponent;
    @Test
    public void checkCreateOrder() {
        //PRECONDITION
        var productName = "testProd1";
        var productPrice = 100;

        var userName = "Van";
        var userPhone = "+71231231231";
        var userBalance = 100;
        var user = userComponent.getOrCreateUser(userName,userPhone);
        var userId = user.getId();

        var pathCreateProduct = "/createGood?name=" + productName + "&price=" + productPrice;
        var responseCreateProduct = TestUtils.callPut(pathCreateProduct).assertThat().statusCode(200);
        var idProduct = Long.valueOf((Integer) responseCreateProduct.extract().body().path("id"));

        var pathCreateAccount ="/createAccount?id="+userId+"&userBalance="+userBalance;
        var pathCreateOrder = "/createOrder?userName=" + userName + "&userPhone=" + userPhone + "&productName=" + productName;



        //TEST

        var responseCreateAccount = TestUtils.callPut(pathCreateAccount).assertThat().statusCode(200);
        var responseCreateOrder = TestUtils.callPost(pathCreateOrder).assertThat().statusCode(200);

        var idOrder = Long.valueOf((Integer) responseCreateOrder.extract().body().path("id"));

        //asserts
        var pathGetUser = "/byPhone?phone=" + userPhone;

        var responseCreateUser = TestUtils.callGet(pathGetUser);

        var idProductInOrder = Long.valueOf((Integer) responseCreateOrder.extract().body().path("productId"));
        var idUserInOrder = Long.valueOf((Integer) responseCreateOrder.extract().body().path("authorId"));
        var idUser = Long.valueOf((Integer) responseCreateUser.extract().body().path("id"));
        var idUserAccount = Long.valueOf((Integer) responseCreateAccount.extract().body().path("userId"));
        var AccBal = Long.valueOf((Integer) responseCreateAccount.extract().body().path("balance"));

        assertThat(responseCreateUser.extract().statusCode()).as("statusCode").isEqualTo(200);
        assertThat(idProductInOrder).as("productId").isEqualTo(idProduct);
        assertThat(idUserInOrder).as("authorId").isEqualTo(idUser);
        assertThat(idUserAccount).as("id").isEqualTo(userId);
        assertThat(AccBal).as("balance").isEqualTo(userBalance);

        //postcondition
        var pathDeleteAccount = "/deleteAccountById?id="+userId;
        TestUtils.callDelete(pathDeleteAccount).statusCode(200);

        var pathDeleteOrder = "/deleteOrderById?id=" + idOrder;
        TestUtils.callDelete(pathDeleteOrder).statusCode(200);

        var pathDeleteProduct = "/deleteProductById?id=" + idProduct;
        TestUtils.callDelete(pathDeleteProduct).statusCode(200);

        var pathDeleteUser = "/deleteUserById?id=" + idUser;
        TestUtils.callDelete(pathDeleteUser).statusCode(200);

    }
}
