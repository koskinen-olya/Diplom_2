import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    public Response createUser(String email, String password, String name) {
        BodyForCreateUser jsonBody = new BodyForCreateUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .post("/api/auth/register");
        return response;
    }

    public Response loginUser(String email, String password) {
        BodyForLoginUser jsonBody = new BodyForLoginUser(email, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .post("/api/auth/login");
        return response;
    }

    public String getToken(String email, String password) {
        Response login = loginUser(email, password);
        String token = login.then().extract().body().path("accessToken");
        return token.substring(7);
    }

    public void deleteUser(String email, String password) {
        String token = getToken(email, password);
        given().auth().oauth2(token).delete("/api/auth/user");
    }

    public Response updateInfoAboutUser(String email, String password, String name, String token) {
        BodyForCreateUser jsonBody = new BodyForCreateUser(email, password, name);
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .patch("/api/auth/user");
        return response;
    }
}
