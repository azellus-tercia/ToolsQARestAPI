import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.CreateUserClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseRunner;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ToolsQATest extends BaseRunner {

    @Test
    public void testGetResponse() {
        boolean responseIsReceived = new CreateUserClass(getTokenAPI())
                .responseReceived("username", "password");

        Assert.assertTrue(responseIsReceived);
    }

    @Test
    public void testCreateUserWithWrongPassword() {
        Response response = new CreateUserClass(getTokenAPI())
                .getResponseCreateUser("username", "password");

        Assert.assertEquals(response.jsonPath().get("code"), "1300");
        Assert.assertEquals(response.jsonPath().get("message"), "Passwords must have at least one non alphanumeric character," +
                " one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z')," +
                " one special character and Password must be eight characters or longer.");
    }

    @Test
    public void getStatusCodTest() {
        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/Books";
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "");

        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
    }

    @Test
    public void IteratingHeaders() {
        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/Books";
        RequestSpecification httpRequest = given();
        Response response = httpRequest.get("");

        Headers headers = response.headers();

        for(Header header : headers) {
//            System.out.println("Key: " + header.getName() + ", Value: " + header.getValue());
        }

        Assert.assertEquals(response.header("Content-Type") , "application/json; charset=utf-8" );
        Assert.assertEquals(response.header("Content-Length") , "4514" );
        Assert.assertEquals(response.header("Server") , "nginx/1.17.10 (Ubuntu)" );
        Assert.assertEquals(response.header("Connection") , "keep-alive" );
        Assert.assertEquals(response.header("X-Powered-By") , "Express" );
        Assert.assertEquals(response.header("ETag") , "W/\"11a2-8zfX++QwcgaCjSU6F8JP9fUd1tY\"" );
    }

    @Test
    public void getBodyBooksTest() {
        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/Books";
        RequestSpecification httpRequest = given();
        Response response = httpRequest.get("viewport");

        Assert.assertTrue(response.getBody().asString().contains("viewport"));
    }

    @Test
    public void verifyJsonResponseTest() {
        String[] actual = {"Git Pocket Guide", "Learning JavaScript Design Patterns", "Designing Evolvable Web APIs with ASP.NET",
                "Speaking JavaScript", "You Don't Know JS", "Programming JavaScript Applications", "Eloquent JavaScript, Second Edition", "Understanding ECMAScript 6"};

        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/Books";
        RequestSpecification httpRequest = given();
        Response response = httpRequest.get("");

        JsonPath jsonPath = response.jsonPath();
        List<String> title = jsonPath.get("books.title");

        Assert.assertEquals(actual, title.toArray());
    }
}
