import files.Payload;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddPlace {
    public static void main(String[] args) {
//       RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
                .log()
                .all()
                .baseUri("https://rahulshettyacademy.com")
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(Payload.addPlace())
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract()
                .response()
                .asString();

        System.out.println(response);

        JsonPath json = new JsonPath(response); // for parsing json
        String placeId = json.getString("place_id");
        System.out.println(placeId);
    }
}
