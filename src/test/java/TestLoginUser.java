import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestLoginUser {
    private final String email;
    private final String password;

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
                {"testTwo@mail.ru", "12345", 200, true},//Проверка логина с существующим пользователем
                {"userOne@mail.ru", "12345", 401, false},//Проверка логина с несуществующим пользователем
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        UserClient userClient = new UserClient();
        userClient.createUser("testTwo@mail.ru", "12345", "userTwo");
        //Удаление учетки для проверки логина с несуществующим пользователем
        boolean userOne = userClient.loginUser("userOne@mail.ru", "12345").then().extract().body().path("success");
        if (userOne)
            userClient.deleteUser("userOne@mail.ru", "12345");
    }

    @Test
    public void testLoginUser() {
        UserClient userClient = new UserClient();
        Response response = userClient.loginUser(email, password);
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
