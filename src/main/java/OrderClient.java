import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final String ORDER = "/api/orders";

    public Response createOrder(String[] ingredients, String token) {
        Order jsonBody = new Order(ingredients);
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .post(ORDER);
        return response;
    }

    public Response getOrders(String token) {
        Response response =
                given()
                        .auth().oauth2(token)
                        .get(ORDER);
        return response;
    }
}
