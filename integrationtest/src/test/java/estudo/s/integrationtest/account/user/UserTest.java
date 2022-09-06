package estudo.s.integrationtest.account.user;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
// import static io.restassured.module.jsv.JsonSchemaValidator.*;
// import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    private static final String BASE_URL = "http://localhost:8080/account/api/v1";

    private static final String API_URL = BASE_URL + "/users";

    private static final String JSON_CONTENT_TYPE = "application/json";

    private static final String EDIT_LINK_PATH = "_links.edit.href";
    
    private List<String> deleteLinks = new ArrayList<>();


    @AfterEach
    public void teardown() {
        for (String link : deleteLinks) {
            delete(link);
        }        

        when().
            get(BASE_URL).
        then().
            body("_embedded", nullValue());

        deleteLinks = new ArrayList<>();
    }
    
    @Test
    public void testInsert() {

        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        Response response = 
            given().
                contentType(JSON_CONTENT_TYPE).
                body(json.toString()).
            when().
                post(API_URL).
            then().
                statusCode(201).
                body("id", notNullValue()).
                body("name", equalTo(json.get("name"))).
                body("password", equalTo(json.get("password"))).
            extract().response();

        String id = response.path("id").toString();
        String responseEditLink = response.path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        deleteLinks.add(responseEditLink);
    }

    @Test
    public void testFindById() {
        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        String id = insertOne(json);

        String responseEditLink = 
            when().
                get(editLink(id)).
            then().
                statusCode(200).
                body("id", equalTo(id)).
                body("name", equalTo(json.get("name"))).
                body("password", equalTo(json.get("password"))).
            extract().path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));
    }

    @Test 
    public void findByIdWhenNotFound() {

        String id = UUID.randomUUID().toString();
        
        when().
            get(editLink(id)).
        then().
            statusCode(204);
    }

    private String insertOne(JSONObject json) {

        Response response = 
            given().
                contentType(JSON_CONTENT_TYPE).
                body(json.toString()).
            post(API_URL).
            then().
                extract().response();

        String id = response.path("id").toString();
        String editLink = response.path(EDIT_LINK_PATH).toString();
            
        deleteLinks.add(editLink);

        return id;
    }

    private String editLink(String id) {
        return API_URL + "/" + id;
    }
    

}
