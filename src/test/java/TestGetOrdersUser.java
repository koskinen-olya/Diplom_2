import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestGetOrdersUser {
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    private final String URL = "https://stellarburgers.nomoreparties.site";
    //Создание рандомного email
    String email = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание рандомного password
    String password = String.format("%s", RandomStringUtils.randomNumeric(5).toLowerCase());
    //Создание рандомного name
    String name = String.format("%s", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        userClient.createUser(email, password, name);
    }

    @Test
    public void testGetOrderWithToken() {
        String token = userClient.getToken(email, password);
        Response response = orderClient.getOrders(token);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    public void testGetOrderWithoutToken() {
        Response response = orderClient.getOrders("");
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(email, password);
    }
}
