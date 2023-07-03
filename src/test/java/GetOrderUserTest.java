import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserStellar;
import model.OrderStellar;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testValue.TestValue;
import user.UserClient;

import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

/**
 5. Получение заказов конкретного пользователя:
 * авторизованный пользователь,
 * неавторизованный пользователь.
 */
public class GetOrderUserTest {
    private UserClient userClient;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя. Ответ 200")
    @Description("Get запрос на ручку api/orders")
    @Step("Получение заказов")
    public void getOrderAuthUser() {
        UserStellar userStellar = new UserStellar(TestValue.TEST_LOGIN_ONE, TestValue.TEST_PASSWORD_ONE, TestValue.TEST_NAME_ONE);
        ValidatableResponse responseCreate = userClient.createUser(userStellar).assertThat().statusCode(HTTP_OK);
        String accessTokenWithBearer = responseCreate.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(TestValue.TEST_BUN);
        ingredients.add(TestValue.TEST_FILLING_ONE);
        ingredients.add(TestValue.TEST_FILLING_TWO);
        OrderStellar orderStellar = new OrderStellar(ingredients);
        ValidatableResponse response = orderClient.orderWithAuth(accessToken, orderStellar)
                .assertThat().statusCode(HTTP_OK);
        ValidatableResponse responseGetOrders = orderClient.getOrderUserAuth(accessToken)
                .assertThat().statusCode(HTTP_OK);
        responseGetOrders.assertThat().body("success", equalTo(true))
                .and()
                .body("orders", not(ingredients.isEmpty()));
    }

    @Test
    @DisplayName("Получение заказов не авторизованного пользователя. Ответ 401")
    @Description("Get запрос на ручку api/orders")
    @Step("Получение заказов")
    public void getOrderNotAuthUser() {
        UserStellar userStellar = new UserStellar(TestValue.TEST_LOGIN_ONE, TestValue.TEST_PASSWORD_ONE, TestValue.TEST_NAME_ONE);
        ValidatableResponse responseCreate = userClient.createUser(userStellar).assertThat().statusCode(HTTP_OK);
        String accessTokenWithBearer = responseCreate.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(TestValue.TEST_BUN);
        ingredients.add(TestValue.TEST_FILLING_ONE);
        ingredients.add(TestValue.TEST_FILLING_TWO);
        OrderStellar orderStellar = new OrderStellar(ingredients);
        ValidatableResponse response = orderClient.orderWithAuth(accessToken, orderStellar)
                .assertThat().statusCode(HTTP_OK);
        ValidatableResponse responseGetOrders = orderClient.getOrderUserNotAuth()
                .assertThat().statusCode(HTTP_UNAUTHORIZED);
        responseGetOrders.assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void clearData() {
        try {
            UserStellar userStellar = new UserStellar(TestValue.TEST_LOGIN_ONE, TestValue.TEST_PASSWORD_ONE, TestValue.TEST_NAME_ONE);
            ValidatableResponse responseLogin = userClient.loginUser(userStellar);
            String accessTokenWithBearer = responseLogin.extract().path("accessToken");
            String accessToken = accessTokenWithBearer.replace("Bearer ", "");
            userClient.deleteUser(accessToken);
        } catch (Exception e) {
            System.out.println("Завершилось без удаления");
        }
    }
}