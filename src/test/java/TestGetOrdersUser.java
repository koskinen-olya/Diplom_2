import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestGetOrdersUser {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        UserClient userClient = new UserClient();
        userClient.createUser("testGetOrders@mail.ru", "12345", "testGetOrders");
    }

    @Test
    public void testGetOrderWithToken() {
        OrderClient orderClient = new OrderClient();
        UserClient userClient = new UserClient();
        String token = userClient.getToken("testGetOrders@mail.ru", "12345");
        Response response = orderClient.getOrders(token);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    public void testGetOrderWithoutToken() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getOrders("");
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
    }

    @After
    public void deleteUser() {
        UserClient userClient = new UserClient();
        userClient.deleteUser("testGetOrders@mail.ru", "12345");
    }
}
