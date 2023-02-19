import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    private final String CREATE_USER = "/api/auth/register";
    private final String LOGIN = "/api/auth/login";
    private final String UPD_USER = "/api/auth/user";

    public Response createUser(String email, String password, String name) {
        CreateUser jsonBody = new CreateUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .post(CREATE_USER);
        return response;
    }

    public Response loginUser(String email, String password) {
        LoginUser jsonBody = new LoginUser(email, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .post(LOGIN);
        return response;
    }

    public String getToken(String email, String password) {
        Response login = loginUser(email, password);
        String token = login.then().extract().body().path("accessToken");
        if (token != null){
        return token.substring(7);}
        else return null;
    }

    public void deleteUser(String email, String password) {
        String token = getToken(email, password);
        if (token != null)
        given().auth().oauth2(token).delete(UPD_USER);
    }

    public Response updateInfoAboutUser(String email, String password, String name, String token) {
        CreateUser jsonBody = new CreateUser(email, password, name);
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonBody)
                        .when()
                        .patch(UPD_USER);
        return response;
    }
}
