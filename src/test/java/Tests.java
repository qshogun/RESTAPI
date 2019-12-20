import com.qshogun.util.HelperMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Tests {

    Properties properties = new Properties();
    private Response res = null;
    private JsonPath jsonPath = null;

    @BeforeClass
    public void setup() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Qshogun\\IdeaProjects\\restassuredproject2\\src\\main\\java\\com\\qshogun\\files\\env.properties");
        properties.load(fileInputStream);
        RestAssured.baseURI = properties.getProperty("HOST");
    }
    @Test
    public void T01_getUsers() throws IOException {
        res = given()
                .when()
                .get("/users")
                .then()
                .assertThat().statusCode(200).and().extract().response();
        System.out.println(HelperMethods.rawToJson(res));
    }
    @Test
    public void T02_getAllPostsFromUser() throws IOException {
        res = given()
                .queryParam("userId", "1")
                .when()
                .get("/posts")
                .then()
                .assertThat().statusCode(200).and().extract().response();
        String resString = res.asString();
        JsonPath js = new JsonPath(resString);
        System.out.println(js);
    }
    @Test
    public void T03_getNumberOfPostsFromUser() throws IOException {
        res = given()
                .queryParam("userId", "1")
                .when()
                .get("/posts")
                .then()
                .assertThat().statusCode(200).and().extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        int numberOfPosts = jsonPath.get("id.size()");
        System.out.println(numberOfPosts);
    }
    @Test
    public void T04_getListOfAllPostTitlesFromUser() throws IOException {
        res = given()
                .queryParam("userId", "1")
                .when()
                .get("/posts")
                .then()
                .assertThat().statusCode(200).and().extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        List<String> listOfAllPostTitles = jsonPath.get("title");
        for(int j=0;j<listOfAllPostTitles.size();j++) {
            System.out.println(listOfAllPostTitles.get(j));
        }
    }
    @Test
    public void T05_getListOfAllCommentsFromUserPost() throws IOException {
        res = given()
                .queryParam("userId", "1")
                .queryParam("postId", "1")
                .when()
                .get("/comments")
                .then()
                .assertThat().statusCode(200).and().extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        List<String> listOfAllComments = jsonPath.get("body");
        for(int j=0;j<listOfAllComments.size();j++) {
            System.out.println(listOfAllComments.get(j));
        }
    }

    @Test
    public void T07_doesPostExistAfterBeingCreated() throws IOException {
        String postBody = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"saaaa\",\n" +
                "    \"body\": \"aaaaaaaa\"\n" +
                "  }";
        res = given()
                .body(postBody)
                .when()
                .post("/posts")
                .then()
                .assertThat().statusCode(201).and().extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        int newPostId = jsonPath.get("id");
        System.out.println(newPostId);
        res = given()
                .queryParam("userId", "1")
                .param("id", newPostId)
                .when()
                .get("/posts")
                .then()
                .extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        ArrayList titleSample = jsonPath.get("title");
        System.out.println(newPostId);
        System.out.println(titleSample);
        Assert.assertFalse(titleSample.isEmpty());
    }
    @Test
    public void T08_isUserNameCorrect() throws IOException {
        baseURI = "https://jsonplaceholder.typicode.com";
        res = given()
                .contentType("application/json")
                .param("id", 1)
                .when()
                .get("/users")
                .then()
                .extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        String userName = jsonPath.get("name").toString();
        Assert.assertEquals("User name doesn't match", "[Leanne Graham]", userName);
    }
    @Test
    public void T09_hasUserStreetHasMoreThanFourCharacters() throws IOException {
        baseURI = "https://jsonplaceholder.typicode.com";
        res = given()
                .param("id", 1)
                .when()
                .get("/users")
                .then()
                .contentType(ContentType.JSON)
                .extract().response();
        jsonPath = HelperMethods.rawToJson(res);
        String streetName = jsonPath.get("address.street").toString();
        int numberOfCharsInStreetName = streetName.toCharArray().length;
        Assert.assertTrue(numberOfCharsInStreetName>4);
        System.out.println(numberOfCharsInStreetName);
    }
}

