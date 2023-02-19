import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestUpdateInfoAboutUser {
    private final String URL = "https://stellarburgers.nomoreparties.site";
    //Создание рандомного email
    static String email = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание рандомного password
    static String password = String.format("%s", RandomStringUtils.randomNumeric(5).toLowerCase());
    //Создание рандомного name
    static String name = String.format("%s", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание email для обновления
    static String emailForUpd = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(5).toLowerCase());
    //Создание password  для обновления
    static String passwordForUpd = String.format("%s", RandomStringUtils.randomNumeric(5).toLowerCase());
    //Создание name  для обновления
    static String nameForUpd = String.format("%s", RandomStringUtils.randomAlphabetic(5).toLowerCase());

    public TestUpdateInfoAboutUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {emailForUpd, password, name},//Проверка обновления поля email
                {email, passwordForUpd, name},//Проверка обновления поля password
                {email, password, nameForUpd},//Проверка обновления поля name
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        UserClient userClient = new UserClient();
        userClient.createUser(email, password, name);
    }

    @Test
    public void testUpdateWithToken() {
        UserClient userClient = new UserClient();
        String token = userClient.getToken(email, password);
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
        userClient.deleteUser(email, password);
    }
}
