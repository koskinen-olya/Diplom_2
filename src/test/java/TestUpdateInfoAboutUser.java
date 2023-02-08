import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestUpdateInfoAboutUser {
    private final String email;
    private final String password;
    private final String name;

    public TestUpdateInfoAboutUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"emailUpdate@mail.ru", "12345", "userForTest"},//Проверка обновления поля email
                {"emailForTest@mail.ru", "54321", "userForTest"},//Проверка обновления поля password
                {"emailForTest@mail.ru", "12345", "nameUpdate"},//Проверка обновления поля name
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        UserClient userClient = new UserClient();
        userClient.createUser("emailForTest@mail.ru", "12345", "userForTest");
    }

    @Test
    public void testUpdateWithToken() {
        UserClient userClient = new UserClient();
        String token = userClient.getToken("emailForTest@mail.ru", "12345");
        Response response = userClient.updateInfoAboutUser(email, password, name, token);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    public void testUpdateWithoutToken() {
        UserClient userClient = new UserClient();
        Response response = userClient.updateInfoAboutUser(email, password, name, "");
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        UserClient userClient = new UserClient();
        boolean user = userClient.loginUser(email, password).then().extract().body().path("success");
        if (user)
            userClient.deleteUser(email, password);
    }
}
