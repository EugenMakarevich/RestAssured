import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AddBook {
    @Test
    public void addBookTest() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        JsonPath addBookResponse = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(Payload.getAddBookBody())
                .when()
                .post("/Library/Addbook.php")
                .then()
                .log()
                .all()
                .extract()
                .response()
                .jsonPath();

        String id = addBookResponse.get("ID");
        System.out.println(id);
    }
}