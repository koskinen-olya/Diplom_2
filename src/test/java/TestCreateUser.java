import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestCreateUser {
    private final String email;
    private final String password;
    private final String name;
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
                {"testTwo@mail.ru", "12345", "userTwo", 200, true},//Проверка создания уникального пользователя
                {"userOne@mail.ru", "12345", "userOne", 403, false},//Проверка создания не уникального пользователя
                {"test@mail.ru", "", "user", 403, false},//Проверка создания пользователя без указания обязательного поля
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        UserClient userClient = new UserClient();
        //Создание учетки для проверки создания не уникального пользователя
        boolean userOne = userClient.loginUser("userOne@mail.ru", "12345").then().extract().body().path("success");
        if (!userOne)
            userClient.createUser("userOne@mail.ru", "12345", "userOne");
        //Удаление учетки для проверки создания уникального пользователя
        boolean userTwo = userClient.loginUser("testTwo@mail.ru", "12345").then().extract().body().path("success");
        if (userTwo)
            userClient.deleteUser("testTwo@mail.ru", "12345");
    }

    @Test
    public void testCreateUser() {
        UserClient userClient = new UserClient();
        Response response = userClient.createUser(email, password, name);
        response.then().assertThat().statusCode(expectedCode);
        response.then().assertThat().body("success", equalTo(expectedSuccess));
    }

    @After
    public void deleteUser() {
        UserClient userClient = new UserClient();
        //Удаление учетки userOne
        boolean userDeleteOne = userClient.loginUser("userOne@mail.ru", "12345").then().extract().body().path("success");
        if (userDeleteOne)
            userClient.deleteUser("userOne@mail.ru", "12345");
        //Удаление учетки userTwo
        boolean userDeleteTwo = userClient.loginUser("testTwo@mail.ru", "12345").then().extract().body().path("success");
        if (userDeleteTwo)
            userClient.deleteUser("testTwo@mail.ru", "12345");
    }
}
