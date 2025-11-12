import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AddBookTest {
    @DataProvider(name = "bookData")
    public Object[][] addBookData() {
        return new Object[][]{
                {"abc", "7777"},
                {"def", "6666"},
                {"gkl", "5555"}
        };
    }

    @Test(dataProvider = "bookData")
    public void addBookTest(String isbn, String aisle) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        JsonPath addBookResponse = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(Payload.getAddBookBody(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath();

        String id = addBookResponse.get("ID");
        System.out.println(id);
    }

    // Delete book
    // 1. Create an instance list variable to collect IDs from addBookTest()
    // 4. Create @DataProvider
    // 2. Create delete response and request
    // 3. Verify: 200 OK, "msg": "book is successfully deleted"
}