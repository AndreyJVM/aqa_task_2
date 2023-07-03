import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testValue.TestValue;
import user.UserClient;

import static java.net.HttpURLConnection.*;

/**
 3. Изменение данных пользователя:
 * с авторизацией,
 * без авторизации,
 */
public class ChangeUserDataTest {
    private UserClient userClient;

    @Before
    @Step("Предусловие.Создание пользователя")
    public void setUp() {
        userClient = new UserClient();
        UserStellar userStellar = new UserStellar(TestValue.TEST_LOGIN_ONE, TestValue.TEST_PASSWORD_ONE, TestValue.TEST_NAME_ONE);
        ValidatableResponse response = userClient.createUser(userStellar);
    }

    @After
    @Step("Постусловие.Удаление пользователя")
    public void clearData() {
        try {
            UserStellar userStellarTwo = new UserStellar(TestValue.TEST_LOGIN_TWO, TestValue.TEST_PASSWORD_TWO, TestValue.TEST_NAME_TWO);
            ValidatableResponse responseLogin = userClient.loginUser(userStellarTwo);
            String accessTokenWithBearer = responseLogin.extract().path("accessToken");
            String accessToken = accessTokenWithBearer.replace("Bearer ", "");
            ValidatableResponse responseDelete = userClient.deleteUser(accessToken);
            System.out.println("удален");
        } catch (Exception e) {
            System.out.println("Пользователь не удалился через постусловие");
        }
    }

    @Test
    @DisplayName("Изменение информации о пользвателе с авторизацией. Ответ 200")
    @Description("Patch запрос на ручку /api/auth/user")
    @Step("Основной шаг - Изменение информации")
    public void updateUserWithAuth() {
        UserStellar userStellar = new UserStellar(TestValue.TEST_LOGIN_ONE, TestValue.TEST_PASSWORD_ONE, TestValue.TEST_NAME_ONE);
        ValidatableResponse responseLogin = userClient.loginUser(userStellar);
        String accessTokenWithBearer = responseLogin.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        UserStellar userStellarTwo = new UserStellar(TestValue.TEST_LOGIN_TWO, TestValue.TEST_PASSWORD_TWO, TestValue.TEST_NAME_TWO);
        ValidatableResponse responseUpdate = userClient.updateUser(accessToken, userStellarTwo)
                .assertThat().statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Изменение информации о пользвателе без авторизации. Ответ 401")
    @Description("Patch запрос на ручку /api/auth/user")
    @Step("Основной шаг - Изменение информации")
    public void updateUserWithoutAuth() {
        UserStellar userStellar = new UserStellar(TestValue.TEST_LOGIN_ONE, TestValue.TEST_PASSWORD_ONE, TestValue.TEST_NAME_ONE);
        ValidatableResponse responseLogin = userClient.loginUser(userStellar);
        String accessTokenWithBearer = responseLogin.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        UserStellar userStellarTwo = new UserStellar(TestValue.TEST_LOGIN_TWO, TestValue.TEST_PASSWORD_TWO, TestValue.TEST_NAME_TWO);
        userClient.updateUserNotAuth(userStellarTwo)
                .assertThat().statusCode(HTTP_UNAUTHORIZED);
        userClient.deleteUser(accessToken);
    }
}