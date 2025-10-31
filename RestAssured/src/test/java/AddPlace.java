import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddPlace {
    public static void main(String[] args) {
        String key = "qaclick123";
        String successUpdateMsg = "Address successfully updated";
        String updatedAddress = "70 winter walk, USA";
        // Add Place
        RestAssured.baseURI = "https://rahulshettyacademy.com";
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

        System.out.println(response);

        JsonPath json = new JsonPath(response); // for parsing json
        String placeId = json.getString("place_id");
        System.out.println(placeId);

        System.out.println(Payload.updatePlace(placeId));

        // Update Place
        given()
                .log()
                .all()
                .queryParam("key", key)
                .queryParam("place_id", placeId)
                .header("Content-Type", "application/json")
                .body(Payload.updatePlace(placeId))
                .when()
                .put("/maps/api/place/update/json")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("msg", equalTo(successUpdateMsg));

        // Get Place
        given()
                .log()
                .all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .header("Content-Type", "application/json")
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("address", equalTo(updatedAddress));
    }
}
