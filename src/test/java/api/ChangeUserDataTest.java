package api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static testValue.TestValue.*;

/**
 3. Изменение данных пользователя:
 * с авторизацией,
 * без авторизации,
 */
public class ChangeUserDataTest {
    private UserClient userClient;
    private UserStellar userStellar;
    private ValidatableResponse response;

    @Before
    @Step("Предусловие.Создание пользователя")
    public void setUp() {
        userClient = new UserClient();
        userStellar = new UserStellar(TEST_LOGIN_ONE, TEST_PASSWORD_ONE, TEST_NAME_ONE);
        response = userClient.createUser(userStellar);
    }

    @Test
    @DisplayName("Изменение информации о пользователе с авторизацией. Ответ 200")
    @Description("Patch запрос на ручку /api/auth/user")
    @Step("Основной шаг - Изменение информации")
    public void updateUserWithAuth() {
        UserStellar userStellar = new UserStellar(TEST_LOGIN_ONE, TEST_PASSWORD_ONE, TEST_NAME_ONE);
        ValidatableResponse responseLogin = userClient.loginUser(userStellar);
        String accessToken = extractToken(responseLogin).replace("Bearer ", "");
        UserStellar userStellarTwo = new UserStellar(TEST_LOGIN_TWO, TEST_PASSWORD_TWO, TEST_NAME_TWO);
        userClient
                .updateUser(accessToken, userStellarTwo)
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение информации о пользвателе без авторизации. Ответ 401")
    @Description("Patch запрос на ручку /api/auth/user")
    @Step("Основной шаг - Изменение информации")
    public void updateUserWithoutAuth() {
        UserStellar userStellar = new UserStellar(TEST_LOGIN_ONE, TEST_PASSWORD_ONE, TEST_NAME_ONE);
        ValidatableResponse responseLogin = userClient.loginUser(userStellar);
        String accessToken = extractToken(responseLogin).replace("Bearer ", "");
        UserStellar userStellarTwo = new UserStellar(TEST_LOGIN_TWO, TEST_PASSWORD_TWO, TEST_NAME_TWO);
        userClient
                .updateUserNotAuth(userStellarTwo)
                .assertThat()
                .statusCode(401);

        userClient.deleteUser(accessToken);
    }

    private String extractToken(ValidatableResponse responseLogin){
        return responseLogin.extract().path("accessToken");
    }

    @After
    @Step("Постусловие.Удаление пользователя")
    public void clearData() {
        try {
            UserStellar userStellarTwo = new UserStellar(TEST_LOGIN_TWO, TEST_PASSWORD_TWO, TEST_NAME_TWO);
            ValidatableResponse responseLogin = userClient.loginUser(userStellarTwo);
            String accessToken = extractToken(responseLogin).replace("Bearer ", "");
            userClient.deleteUser(accessToken);
        } catch (Exception e) {
            System.out.println("Пользователь не удалился через постусловие");
        }
    }
}