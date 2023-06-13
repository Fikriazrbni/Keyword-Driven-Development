import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.asynchttpclient.Request;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestApi {
    public static String strBaseEndpoint = "https://airportgap.dev-tester.com/api";
    public static String token = "hVi8aavdk36p4P5Fbd3mgs6H";

    @Test
    public void getApi() {
        // Set base URL of the API
        RestAssured.baseURI = strBaseEndpoint;

        // Send GET request to the API endpoint
        Response response = RestAssured.get("/airports");

        // Verify the status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(statusCode);

        // Get Body
        String text = response.asPrettyString();
        String json = response.getBody().asString();
        Assert.assertTrue(text.contains("GKA"), "Not Found");
        System.out.println(text);
    }

    @Test
    public void getWithId() {
        // Set base URL of the API
        RestAssured.baseURI = strBaseEndpoint;

        // Send GET request to the API endpoint
        Response response = RestAssured.get("/airports/KIX");

        // Verify the status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(statusCode);

        // Get Body
        String text = response.asPrettyString();
        String json = response.getBody().asString();
        Assert.assertTrue(text.contains("KIX"), "Not Found");
        System.out.println(text);
    }

    @Test
    public void postDistance() {
        // Set base URL of the API
        RestAssured.baseURI = strBaseEndpoint;

        String from = "KIY";
        String to = "GKA";

        // Send GET request to the API endpoint
        Response response = RestAssured.given()
                .formParam("from", from)
                .formParam("to", to)
                .post("/airports/distance");

        // Verify the status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(statusCode);

        // Get Body
        String text = response.asPrettyString();
        Assert.assertTrue(text.contains("kilometers"), "Not Found");
        System.out.println(text);
    }
    @Test
    public void postReqToken() {
        // Set base URL of the API
        RestAssured.baseURI = strBaseEndpoint;

        String email = "testapi@gmail.com";
        String password = "password";

        // Send GET request to the API endpoint
        Response response = RestAssured.given()
                .formParam("email", email)
                .formParam("password", password)
                .post("/tokens");

        // Verify the status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(statusCode);

        // Get Body
        String text = response.asPrettyString();
        Assert.assertTrue(text.contains("token"), "Not Found");
        System.out.println(text);
    }
    @Test
    public void addFavorites() {
        RestAssured.useRelaxedHTTPSValidation();
        // Set base URL of the API
        RestAssured.baseURI = strBaseEndpoint;
        RequestSpecification specification = RestAssured.given();

        specification.header("Authorization","Bearer token="+token);

        String portId = "22";
        String note = "myFav Airport";
        // Send get request to the API endpoint
        Response response = specification.formParam("airport_id"+portId)
                        .formParam("note"+note).post("/favorites");

        // Verify the status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(statusCode);

        // Get Body
        String text = response.asPrettyString();
        Assert.assertTrue(text.contains("data"), "Not Found");
        System.out.println(text);
    }
    @Test
    public void favorites() {
        RestAssured.useRelaxedHTTPSValidation();
        // Set base URL of the API
        RestAssured.baseURI = strBaseEndpoint;
        RequestSpecification specification = RestAssured.given();

        specification.header("Authorization","Token "+token);

        // Send get request to the API endpoint
        Response response = specification.get("/favorites");

        // Verify the status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(statusCode);

        // Get Body
        String text = response.asPrettyString();
        Assert.assertTrue(text.contains("data"), "Not Found");
        System.out.println(text);
    }
}