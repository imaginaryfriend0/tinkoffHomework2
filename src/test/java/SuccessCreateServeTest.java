import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuccessCreateServeTest {
    @Test
    public void checkCreateServe() {
        //PRECONDITION

        //Определяем переменные атрибутов создаваемой услуги.
        var serveName="testServe";
        float servePrice=100;

        //Определяем переменные, с помощью которых будем проверять услугу на существование.
        var pathGetServe = "/byProductName?productName="+serveName;
        var responseGetServe = TestUtils.callGet(pathGetServe);
        int ff = responseGetServe.extract().statusCode();

        //Проверяем найдена ли услуга с таким же названием, если да, т.е. код 200 - удаляем.
        if(ff == 200){
            var idServe = Long.valueOf((Integer) responseGetServe.extract().body().path("id"));
            var pathDeleteServe = "/deleteProductById?id="+idServe;
            TestUtils.callDelete(pathDeleteServe);
        }

        //Если услуга не найдена - создаем услугу с указанными параметрами.
        var pathCreateServe = "/addNewServe?name=" + serveName + "&price=" + servePrice;
        var responseCreateServe = TestUtils.callPut(pathCreateServe).assertThat().statusCode(200);
        var id1Serve = Long.valueOf((Integer) responseCreateServe.extract().body().path("id"));
        var pathDeleteServe = "/deleteProductById?id="+id1Serve;

        //Определяем переменные, с помощью которых будем проверять правильность заполнения полей.
        var CreatedServeName = responseCreateServe.extract().body().path("name");
        float CreatedServePrice = responseCreateServe.extract().body().path("price");

        //TEST

            //Проверяем, что услуга добавилась с указанными параметрами.
            assertThat(CreatedServeName).as("name").isEqualTo(serveName);
            assertThat(CreatedServePrice).as("price").isEqualTo(servePrice);

        //POSTCONDITION

            //Удаляем услугу.
        TestUtils.callDelete(pathDeleteServe);
    }
}