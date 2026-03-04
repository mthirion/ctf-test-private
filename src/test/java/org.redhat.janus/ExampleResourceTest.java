package org.redhat.janus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    private String michael="{\"id\": \"1\", \"name\": \"Michael\", \"lastname\": \"Thirion\", \"email\": \"mthirion@redhat.com\"}";
    private String rachid="{\"id\": \"2\", \"name\": \"Rachid\", \"lastname\": \"Snoussi\", \"email\": \"snoussi@redhat.com\"}";

    @Test
    public void testEndpoint() {
        given()
                .when().get("/customers/1")
                .then()
                .statusCode(200)
                .body(is(michael));
    }

}
