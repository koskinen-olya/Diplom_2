import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestLoginUser {
    UserClient userClient = new UserClient();
    private static final String URL = "https://stellarburgers.nomoreparties.site";
    //Создание рандомного email
    static String email = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание рандомного password
    static String password = String.format("%s", RandomStringUtils.randomNumeric(6).toLowerCase());
    //Создание рандомного name
    static String name = String.format("%s", RandomStringUtils.randomAlphabetic(5).toLowerCase());

    private final int expectedCode;
    private final boolean expectedSuccess;

    public TestLoginUser(String email, String password, int expectedCode, boolean expectedSuccess) {
        this.email = email;
        this.password = password;
        this.expectedCode = expectedCode;
        this.expectedSuccess = expectedSuccess;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {email, password, 200, true},//Проверка логина с существующим пользователем
                {email, password, 401, false},//Проверка логина с несуществующим пользователем
        };
    }

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = URL;
        UserClient userClient = new UserClient();
        userClient.createUser(email, password, name);
    }

    @Test
    public void testLoginUser() {
        Response response = userClient.loginUser(email, password);
        response.then().assertThat().statusCode(expectedCode);
        response.then().assertThat().body("success", equalTo(expectedSuccess));
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(email, password);
    }
}
