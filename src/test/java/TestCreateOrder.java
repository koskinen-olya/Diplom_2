import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    private final String[] ingredients;
    private final int expectedCode;

    public TestCreateOrder(String[] ingredients, int expectedCode) {
        this.ingredients = ingredients;
        this.expectedCode = expectedCode;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {new String[]{"61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa70"}, 200}, //Проверка создания заказа с корректными игредиентами
                {new String[]{"61c0c5a71d19f3434a70"}, 500}, //Проверка создания заказа с некорректным ингредиентом
                {new String[]{}, 400},//Проверка создания заказа без ингредиентов
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        UserClient userClient = new UserClient();
        userClient.createUser("testOrders@mail.ru", "12345", "testOrders");
    }

    @Test
    public void testCreateOrderWithToken() {
        OrderClient orderClient = new OrderClient();
        UserClient userClient = new UserClient();
        String token = userClient.getToken("testOrders@mail.ru", "12345");
        Response response = orderClient.createOrder(ingredients, token);
        response.then().assertThat().statusCode(expectedCode);
    }

    @Test
    public void testCreateOrderWithoutToken() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.createOrder(ingredients, "");
        response.then().assertThat().statusCode(expectedCode);
    }

    @After
    public void deleteUser() {
        UserClient userClient = new UserClient();
        userClient.deleteUser("testOrders@mail.ru", "12345");
    }
}
