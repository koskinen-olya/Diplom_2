import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    //Создание рандомного email
    String email = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание рандомного password
    String password = String.format("%s", RandomStringUtils.randomNumeric(5).toLowerCase());
    //Создание рандомного name
    String name = String.format("%s", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    private final String URL = "https://stellarburgers.nomoreparties.site";
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
        RestAssured.baseURI = URL;
        userClient.createUser(email, password, name);
    }

    @Test
    public void testCreateOrderWithToken() {
        String token = userClient.getToken(email, password);
        Response response = orderClient.createOrder(ingredients, token);
        response.then().assertThat().statusCode(expectedCode);
    }

    @Test
    public void testCreateOrderWithoutToken() {
        Response response = orderClient.createOrder(ingredients, "");
        response.then().assertThat().statusCode(expectedCode);
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(email, password);
    }
}
