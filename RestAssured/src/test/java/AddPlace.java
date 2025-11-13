import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.assertEquals;

public class AddPlace {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String key = "qaclick123";
        String successUpdateMsg = "Address successfully updated";
        String updatedAddress = "70 winter walk, USA";

        // Add Place
        String response =
                given()
                        .log()
                        .all()
                        .queryParam("key", key)
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

        JsonPath json = new JsonPath(response); // for parsing json
        String placeId = json.getString("place_id");

        // Add place (JSON file)
        String jsonBody;
        try {
            jsonBody = new String(Files.readAllBytes(Paths.get("C:\\Users\\EugenMakarevich\\IdeaProjects\\RestAssuredUdemi\\RestAssured\\src\\main\\resources\\addPlace.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        given()
                .log()
                .all()
                .queryParam("key", key)
                .header("Content-Type", "application/json")
                .body(jsonBody)
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

        // Update Place
        given()
                .log()
                .all()
                .queryParam("key", key)
                .queryParam("place_id", placeId)
                .header("Content-Type", "application/json")
                .body(Payload.updatePlace(placeId, updatedAddress))
                .when()
                .put("/maps/api/place/update/json")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("msg", equalTo(successUpdateMsg));

        // Get Place
        String getResponseString = given()
                .log()
                .all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .log()
                .all()
                .extract()
                .asString();

        JsonPath getResponseJson = new JsonPath(getResponseString);
        String actualAddress = getResponseJson.getString("address");
        assertEquals(actualAddress, updatedAddress);
    }
}
