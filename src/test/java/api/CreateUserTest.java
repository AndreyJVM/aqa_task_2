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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static testValue.TestValue.*;

/**
 * 1. Создание пользователя:
 * создать уникального пользователя;
 * создать пользователя, который уже зарегистрирован;
 * создать пользователя и не заполнить одно из обязательных полей.
 */
public class CreateUserTest {
    private UserClient userClient;
    private ValidatableResponse responseLogin;
    private UserStellar userStellar;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userStellar = new UserStellar(TEST_LOGIN_ONE, TEST_PASSWORD_ONE, TEST_NAME_ONE);
        responseLogin = userClient.createUser(userStellar);
    }

    @Test
    @DisplayName("Создать уникального пользователя. Ответ 200 ОК / Проверка body")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUniqueUserAndCheckBodyTest() {
        responseLogin
                .statusCode(200)
                .body("user.email", equalTo(TEST_LOGIN_ONE))
                .body("user.name", equalTo(TEST_NAME_ONE))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован. Ответ 403 Forbidden / Проверка body")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createRegisteredUserAndCheckBodyTest() {
        userClient.createUser(userStellar)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить пароль. Ответ 403")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUserWithoutPasswordTest() {
        userClient.createUser(new UserStellar(TEST_LOGIN_ONE, null, TEST_NAME_ONE))
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить имя почты. Ответ 403")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUserWithoutEmailTest() {
        userClient.createUser(new UserStellar(null, TEST_PASSWORD_ONE, TEST_NAME_ONE))
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить имя пользователя. Ответ 403")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUserWithoutNameTest() {
        userClient.createUser(new UserStellar(TEST_NAME_ONE, TEST_PASSWORD_ONE, null))
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    @Step("Постусловие.Удаление пользователя")
    public void clearData() {
        try {
            String accessTokenWithBearer = responseLogin.extract().path("accessToken");
            String accessToken = accessTokenWithBearer.replace("Bearer ", "");
            userClient.deleteUser(accessToken);
            System.out.println("удален");
        } catch (Exception e) {
            System.out.println("Пользователь не удалился. Возможно ошибка при создании");
        }
    }
}