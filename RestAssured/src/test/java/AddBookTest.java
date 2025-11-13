import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static files.Payload.getDeleteBookBody;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddBookTest {
    private List<String> idList = new ArrayList();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

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
        idList.add(id);
    }

    @DataProvider(name = "bookIDs")
    public Iterator<String> BookID() {
        return idList.iterator();
    }

    @Test(dataProvider = "bookIDs")
    public void deleteBookTest(String id) {
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(getDeleteBookBody(id))
                .when()
                .delete("Library/DeleteBook.php")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("msg", equalTo("book is successfully deleted"));
    }
}