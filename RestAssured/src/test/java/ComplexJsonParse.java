import io.restassured.path.json.JsonPath;

import static files.Payload.getCoursePriceResponse;
import static org.testng.AssertJUnit.assertEquals;

public class ComplexJsonParse {

    public static void main(String[] args) {
        JsonPath json = new JsonPath(getCoursePriceResponse());

//1. Print No of courses returned by API
        int numOfCourses = json.getInt("courses.size()");
        System.out.println("No of courses = " + numOfCourses);

//2.Print Purchase Amount
        int purchaseAmount = json.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase Amount = " + purchaseAmount);

//3. Print Title of the first course
        String titleOfFirstCourse = json.getString("courses[0].title");
        System.out.println("Title of the first course - " + titleOfFirstCourse);

//4. Print All course titles and their respective Prices
        for (int i = 0; i < numOfCourses; i++) {
            System.out.println(json.getString("courses[" + i + "].title"));
            System.out.println(json.getInt("courses[" + i + "].price"));
        }

//5. Print no of copies sold by RPA Course
        for (int i = 0; i < numOfCourses; i++) {
            String RpaTitle = "RPA";
            if (json.getString("courses[" + i + "].title").equals(RpaTitle)) {
                System.out.println("Number of RPA copies: " + json.getString("courses[" + i + "].copies"));
                break;
            }
        }

//6. Verify if Sum of all Course prices matches with Purchase Amount
        int totalPrice = 0;
        for (int i = 0; i < numOfCourses; i++) {
            int price = json.getInt("courses[" + i + "].price");
            int numOfCopies = json.getInt("courses[" + i + "].copies");
            totalPrice += price * numOfCopies;
        }
        assertEquals(totalPrice, purchaseAmount);
    }
}
