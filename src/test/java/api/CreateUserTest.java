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

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
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
        responseLogin.assertThat().statusCode(HTTP_OK);
        responseLogin.assertThat().body("user.email", equalTo(TEST_LOGIN_ONE))
                .and()
                .assertThat().body("user.name", equalTo(TEST_NAME_ONE));
        responseLogin.assertThat().body("accessToken", startsWith("Bearer "));
        responseLogin.assertThat().body("refreshToken", notNullValue());
        responseLogin.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован. Ответ 403 Forbidden / Проверка body")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createRegisteredUserAndCheckBodyTest() {
        ValidatableResponse responseTwo = userClient.createUser(userStellar)
                .assertThat().statusCode(HTTP_FORBIDDEN);
        responseTwo.assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить пароль. Ответ 403")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUserWithoutPasswordTest() {
        try {
            ValidatableResponse responseLoginNotPassword = userClient.createUser(new UserStellar(TEST_LOGIN_ONE, null, TEST_NAME_ONE))
                    .assertThat().statusCode(HTTP_FORBIDDEN);
            responseLoginNotPassword.assertThat().body("success", equalTo(false))
                    .and().body("message", equalTo("Email, password and name are required fields"));
        } catch (Exception e) {
            ValidatableResponse responseLoginNotPassword = userClient.createUser(new UserStellar(TEST_LOGIN_ONE, null, TEST_NAME_ONE));
            String accessTokenWithBearer = responseLoginNotPassword.extract().path("accessToken");
            String accessToken = accessTokenWithBearer.replace("Bearer ", "");
            userClient.deleteUser(accessToken);
            System.out.println("удален");
        }
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить имя почты. Ответ 403")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUserWithoutEmailTest() {
        try {
            ValidatableResponse responseLoginNotEmail = userClient.createUser(new UserStellar(null, TEST_PASSWORD_ONE, TEST_NAME_ONE))
                    .assertThat().statusCode(HTTP_FORBIDDEN);
            responseLoginNotEmail.assertThat().body("success", equalTo(false))
                    .and().body("message", equalTo("Email, password and name are required fields"));
        } catch (Exception e) {
            ValidatableResponse responseLoginNotEmail = userClient.createUser(new UserStellar(null, TEST_PASSWORD_ONE, TEST_NAME_ONE));
            String accessTokenWithBearer = responseLoginNotEmail.extract().path("accessToken");
            String accessToken = accessTokenWithBearer.replace("Bearer ", "");
            userClient.deleteUser(accessToken);
            System.out.println("удален");
        }

    }

    @Test
    @DisplayName("Создать пользователя и не заполнить имя пользователя. Ответ 403")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание пользователя")
    public void createUserWithoutNameTest() {
        try {
            ValidatableResponse responseLoginNotName = userClient.createUser(new UserStellar(TEST_NAME_ONE, TEST_PASSWORD_ONE, null))
                    .assertThat().statusCode(HTTP_FORBIDDEN);
            responseLoginNotName.assertThat().body("success", equalTo(false))
                    .and().body("message", equalTo("Email, password and name are required fields"));
        } catch (Exception e) {
            ValidatableResponse responseLoginNotName = userClient.createUser(new UserStellar(TEST_NAME_ONE, TEST_PASSWORD_ONE, null));
            String accessTokenWithBearer = responseLoginNotName.extract().path("accessToken");
            String accessToken = accessTokenWithBearer.replace("Bearer ", "");
            userClient.deleteUser(accessToken);
            System.out.println("удален");
        }
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