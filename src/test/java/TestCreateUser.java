import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestCreateUser {
    private final String URL = "https://stellarburgers.nomoreparties.site";
    //Создание рандомного email
    static String email = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание рандомного password
    static String password = String.format("%s", RandomStringUtils.randomNumeric(5).toLowerCase());
    //Создание рандомного name
    static String name = String.format("%s", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    private final int expectedCode;
    private final boolean expectedSuccess;

    public TestCreateUser(String email, String password, String name, int expectedCode, boolean expectedSuccess) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.expectedCode = expectedCode;
        this.expectedSuccess = expectedSuccess;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {email, password, name, 200, true},//Проверка создания уникального пользователя
                {email, password, name, 403, false},//Проверка создания не уникального пользователя
                {email, "", name, 403, false},//Проверка создания пользователя без указания обязательного поля
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @Test
    public void testCreateUser() {
        UserClient userClient = new UserClient();
        Response response = userClient.createUser(email, password, name);
        response.then().assertThat().statusCode(expectedCode);
        response.then().assertThat().body("success", equalTo(expectedSuccess));
    }

    @AfterClass
    public static void deleteUser() {
        UserClient userClient = new UserClient();
        userClient.deleteUser(email, password);
    }
}
