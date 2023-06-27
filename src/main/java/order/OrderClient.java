package order;

import io.restassured.response.ValidatableResponse;
import model.BaseReqSpecURI;
import model.OrderStellar;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseReqSpecURI {


    public ValidatableResponse orderWithoutAuth(OrderStellar orderStellar) {
        return given()
                .spec(getBaseReqSpec())
                .body(orderStellar)
                .post("/api/orders")
                .then();

    }

    public ValidatableResponse orderWithAuth(String accessToken, OrderStellar orderStellar) {
        return given()
                .spec(getBaseReqSpec())
                .body(orderStellar)
                .auth().oauth2(accessToken)
                .post("/api/orders")
                .then();
    }

    public ValidatableResponse getOrderUserAuth(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .auth().oauth2(accessToken)
                .get("/api/orders")
                .then();
    }

    public ValidatableResponse getOrderUserNotAuth() {
        return given()
                .spec(getBaseReqSpec())
                .get("/api/orders")
                .then();
    }
}