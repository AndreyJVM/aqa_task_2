package order;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
/**
 * Содержит основную ссылку на Stellar Burgers для заказа и настройку через getBaseReqSpec
 */
public class OrderRestClient {
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
