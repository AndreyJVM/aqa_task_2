package user;

import io.restassured.response.ValidatableResponse;
import model.BaseReqSpecURI;
import model.UserStellar;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseReqSpecURI {


    public ValidatableResponse createUser(UserStellar userStellar) {
        return given()
                .spec(getBaseReqSpec())
                .body(userStellar)
                .when()
                .post("/api/auth/register")
                .then();
    }

    public ValidatableResponse loginUser(UserStellar userStellar) {
        return given()
                .spec(getBaseReqSpec())
                .body(userStellar)
                .when()
                .post("/api/auth/login")
                .then();
    }

    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .auth().oauth2(accessToken)
                .delete("/api/auth/user")
                .then();
    }

    public ValidatableResponse updateUser(String accessToken, UserStellar userStellar) {
        return given()
                .spec(getBaseReqSpec())
                .body(userStellar)
                .auth().oauth2(accessToken)
                .patch("/api/auth/user")
                .then();
    }

    public ValidatableResponse updateUserNotAuth(UserStellar userStellar) {
        return given()
                .spec(getBaseReqSpec())
                .body(userStellar)
                .patch("/api/auth/user")
                .then();
    }
}