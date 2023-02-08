import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public Response createOrder(String[] ingredients, String token) {
        BodyForCreateOrder jsonBody = new BodyForCreateOrder(ingredients);
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .post("/api/orders");
        return response;
    }

    public Response getOrders(String token) {
        Response response =
                given()
                        .auth().oauth2(token)
                        .get("/api/orders");
        return response;
    }
}
