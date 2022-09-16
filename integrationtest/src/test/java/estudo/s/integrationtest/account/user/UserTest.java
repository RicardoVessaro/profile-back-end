package estudo.s.integrationtest.account.user;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import estudo.s.ipsumintegrationtest.constants.ExpectedMessage;
import estudo.s.ipsumintegrationtest.constants.Message;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
// import static io.restassured.module.jsv.JsonSchemaValidator.*;
// import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static estudo.s.ipsumintegrationtest.constants.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    private static final String BASE_URL = "http://localhost:8080";

    private static final String REST_URL = "/account/api/v1";

    private static final String API_URL = BASE_URL + REST_URL + "/users";

    private static final String JSON_CONTENT_TYPE = "application/json";

    private static final String EDIT_LINK_PATH = "_links.edit.href";

    private static final String EMBEDDED_DATA_NAME = "userDTOList";
    
    private List<String> deleteLinks = new ArrayList<>();


    @AfterEach
    public void teardown() {
        for (String link : deleteLinks) {
            delete(link);
        }        

        when().
            get(API_URL).
        then().
            statusCode(OK.code).
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
                statusCode(CREATED.code).
                body("id", notNullValue()).
                body("name", equalTo(json.get("name"))).
                body("password", equalTo(json.get("password"))).
            extract().response();

        String id = response.path("id").toString();
        String responseEditLink = response.path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        when().
            get(responseEditLink)
        .then().
            statusCode(OK.code).
            body("id", equalTo(id)).
            body("name", equalTo(json.get("name"))).
            body("password", equalTo(json.get("password")));

        deleteLinks.add(responseEditLink);
    }

    @Test
    public void testRequiredFieldOnInsert() {

        given().
            contentType(JSON_CONTENT_TYPE).
            body(new JSONObject().toString()).
        when().
            post(API_URL).
        then().
            statusCode(BAD_REQUEST.code).
            body("httpStatus", equalTo(BAD_REQUEST.description)).
            body("httpStatusCode", equalTo(BAD_REQUEST.code)).
            body("message", equalTo(new ExpectedMessage(Message.REQUIRED_FIELDS, "User", "\nName\nPassword").toString()));    
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
                statusCode(OK.code).
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
            statusCode(NO_CONTENT.code);
    }

    @Test 
    public void testUpdate() {
        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        String id = insertOne(json);

        json = new JSONObject()
            .put("id", id)
            .put("name", "update test")
            .put("password", "1111");

        String responseEditLink = given().
            contentType(JSON_CONTENT_TYPE).
            body(json.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(OK.code).
            body("id", equalTo(id)).
            body("name", equalTo(json.get("name"))).
            body("password", equalTo(json.get("password")))
        .extract().path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        when().
            get(responseEditLink)
        .then().
            statusCode(OK.code).
            body("id", equalTo(id)).
            body("name", equalTo(json.get("name"))).
            body("password", equalTo(json.get("password")));
    }

    @Test 
    public void testUpdateWhenNotFound() {
        String id = UUID.randomUUID().toString();

        JSONObject json = new JSONObject()
            .put("id", id)
            .put("name", "update test")
            .put("password", "1111");

        given().
            contentType(JSON_CONTENT_TYPE).
            body(json.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(NOT_FOUND.code).
            body("httpStatus", equalTo(NOT_FOUND.description)).
            body("httpStatusCode", equalTo(NOT_FOUND.code)).
            body("message", equalTo(new ExpectedMessage(Message.ENTITY_NOT_FOUND, id).toString()));
    }

    @Test
    public void testUpdateWhenIdFromBodyDiffersFromPathId() {
        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        String id = insertOne(json);

        UUID randomUUID = UUID.randomUUID();

        json = new JSONObject()
            .put("id", randomUUID)
            .put("name", "update test")
            .put("password", "1111");

        given().
            contentType(JSON_CONTENT_TYPE).
            body(json.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(BAD_REQUEST.code).
            body("httpStatus", equalTo(BAD_REQUEST.description)).
            body("httpStatusCode", equalTo(BAD_REQUEST.code)).
            body("message", equalTo(new ExpectedMessage(Message.PATH_ID_DIFFERENT_ENTITY_ID, id, randomUUID).toString()));
    }

    @Test
    public void testUpdateWhenIdFromBodyIsNull() {
        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        String id = insertOne(json);

        json = new JSONObject()
            .put("name", "update test")
            .put("password", "1111");

        String responseEditLink = given().
            contentType(JSON_CONTENT_TYPE).
            body(json.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(OK.code).
            body("id", equalTo(id)).
            body("name", equalTo(json.get("name"))).
            body("password", equalTo(json.get("password")))
        .extract().path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        when().
            get(responseEditLink)
        .then().
            statusCode(OK.code).
            body("id", equalTo(id)).
            body("name", equalTo(json.get("name"))).
            body("password", equalTo(json.get("password")));
    }

    @Test
    public void testRequiredFieldOnUpdate() {
        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        String id = insertOne(json);

        JSONObject putJson = new JSONObject()
            .put("id", id);
            
        given().
            contentType(JSON_CONTENT_TYPE).
            body(putJson.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(BAD_REQUEST.code).
            body("httpStatus", equalTo(BAD_REQUEST.description)).
            body("httpStatusCode", equalTo(BAD_REQUEST.code)).
            body("message", equalTo(new ExpectedMessage(Message.REQUIRED_FIELDS, "User", "\nName\nPassword").toString()));    
    }

    @Test
    public void testDelete() {
        JSONObject json = new JSONObject()
            .put("name", "Integration Test")
            .put("password", "0000");

        String id = insertOne(json);

        when().
            delete(editLink(id)).
        then().
            statusCode(NO_CONTENT.code);
    }

    @Test
    public void testDeleteWhenNotFound() {
        String id = UUID.randomUUID().toString();

        when().
            delete(editLink(id)).
        then().
            statusCode(NOT_FOUND.code).
            body("httpStatus", equalTo(NOT_FOUND.description)).
            body("httpStatusCode", equalTo(NOT_FOUND.code)).
            body("message", equalTo(new ExpectedMessage(Message.ENTITY_NOT_FOUND, id).toString()));   
    }

    @Test
    public void testFindFirstPage() {
        JSONObject json0 = new JSONObject()
            .put("name", "Integration Test 0")
            .put("password", "0000");

        JSONObject json1 = new JSONObject()
            .put("name", "Integration Test 1")
            .put("password", "1111");

        JSONObject json2 = new JSONObject()
            .put("name", "Integration Test 2")
            .put("password", "2222");

        String id0 = insertOne(json0);
        insertOne(json1);
        insertOne(json2);

        String embeddedPath = "_embedded."+EMBEDDED_DATA_NAME;

        when().
            get(paginationLink(0, 1)).
        then().
            statusCode(OK.code).
            body("_embedded", notNullValue()).
            body(embeddedPath, notNullValue()).
            body(embeddedPath+".id", hasItem(id0)).
            body(embeddedPath+".name", hasItem(json0.get("name"))).
            body(embeddedPath+".password", hasItem(json0.get("password"))).
            body(embeddedPath+"."+EDIT_LINK_PATH, hasItem(editLink(id0))).

            
            body("_links", notNullValue()).
            body("_links.first", notNullValue()).
            body("_links.first.href", equalTo(paginationLink(0, 1))).

            body("_links.prev", nullValue()).

            body("_links.self", notNullValue()).
            body("_links.self.href", equalTo(paginationLink(0, 1))).

            body("_links.next", notNullValue()).
            body("_links.next.href", equalTo(paginationLink(1, 1))).

            body("_links.last", notNullValue()).
            body("_links.last.href", equalTo(paginationLink(2, 1))).

            body("_links.create", notNullValue()).
            body("_links.create.href", equalTo(API_URL)).


            body("page", notNullValue()).
            body("page.size", equalTo(1)).
            body("page.totalElements", equalTo(3)).
            body("page.totalPages", equalTo(3)).
            body("page.number", equalTo(0));
    }

    @Test
    public void testFindMidPage() {
        JSONObject json0 = new JSONObject()
            .put("name", "Integration Test 0")
            .put("password", "0000");

        JSONObject json1 = new JSONObject()
            .put("name", "Integration Test 1")
            .put("password", "1111");

        JSONObject json2 = new JSONObject()
            .put("name", "Integration Test 2")
            .put("password", "2222");

        insertOne(json0);
        String id1 = insertOne(json1);
        insertOne(json2);

        String embeddedPath = "_embedded."+EMBEDDED_DATA_NAME;

        when().
            get(paginationLink(1, 1)).
        then().
            statusCode(OK.code).
            body("_embedded", notNullValue()).
            body(embeddedPath, notNullValue()).
            body(embeddedPath+".id", hasItem(id1)).
            body(embeddedPath+".name", hasItem(json1.get("name"))).
            body(embeddedPath+".password", hasItem(json1.get("password"))).
            body(embeddedPath+"."+EDIT_LINK_PATH, hasItem(editLink(id1))).

            
            body("_links", notNullValue()).
            body("_links.first", notNullValue()).
            body("_links.first.href", equalTo(paginationLink(0, 1))).

            body("_links.prev", notNullValue()).
            body("_links.prev.href", equalTo(paginationLink(0, 1))).

            body("_links.self", notNullValue()).
            body("_links.self.href", equalTo(paginationLink(1, 1))).

            body("_links.next", notNullValue()).
            body("_links.next.href", equalTo(paginationLink(2, 1))).

            body("_links.last", notNullValue()).
            body("_links.last.href", equalTo(paginationLink(2, 1))).

            body("_links.create", notNullValue()).
            body("_links.create.href", equalTo(API_URL)).


            body("page", notNullValue()).
            body("page.size", equalTo(1)).
            body("page.totalElements", equalTo(3)).
            body("page.totalPages", equalTo(3)).
            body("page.number", equalTo(1));
    }

    @Test
    public void testFindLastPage() {
        JSONObject json0 = new JSONObject()
            .put("name", "Integration Test 0")
            .put("password", "0000");

        JSONObject json1 = new JSONObject()
            .put("name", "Integration Test 1")
            .put("password", "1111");

        JSONObject json2 = new JSONObject()
            .put("name", "Integration Test 2")
            .put("password", "2222");

        insertOne(json0);
        insertOne(json1);
        String id2 = insertOne(json2);

        String embeddedPath = "_embedded."+EMBEDDED_DATA_NAME;

        when().
            get(paginationLink(2, 1)).
        then().
            statusCode(OK.code).
            body("_embedded", notNullValue()).
            body(embeddedPath, notNullValue()).
            body(embeddedPath+".id", hasItem(id2)).
            body(embeddedPath+".name", hasItem(json2.get("name"))).
            body(embeddedPath+".password", hasItem(json2.get("password"))).
            body(embeddedPath+"."+EDIT_LINK_PATH, hasItem(editLink(id2))).

            
            body("_links", notNullValue()).
            body("_links.first", notNullValue()).
            body("_links.first.href", equalTo(paginationLink(0, 1))).

            body("_links.prev", notNullValue()).
            body("_links.prev.href", equalTo(paginationLink(1, 1))).

            body("_links.self", notNullValue()).
            body("_links.self.href", equalTo(paginationLink(2, 1))).

            body("_links.next", nullValue()).

            body("_links.last", notNullValue()).
            body("_links.last.href", equalTo(paginationLink(2, 1))).


            body("_links.create", notNullValue()).
            body("_links.create.href", equalTo(API_URL)).


            body("page", notNullValue()).
            body("page.size", equalTo(1)).
            body("page.totalElements", equalTo(3)).
            body("page.totalPages", equalTo(3)).
            body("page.number", equalTo(2));
    }

    @Test
    public void testFindUniquePage() {
        JSONObject json0 = new JSONObject()
            .put("name", "Integration Test 0")
            .put("password", "0000");

        JSONObject json1 = new JSONObject()
            .put("name", "Integration Test 1")
            .put("password", "1111");

        JSONObject json2 = new JSONObject()
            .put("name", "Integration Test 2")
            .put("password", "2222");

        String id0 = insertOne(json0);
        String id1 = insertOne(json1);
        String id2 = insertOne(json2);

        String embeddedPath = "_embedded."+EMBEDDED_DATA_NAME;

        when().
            get(API_URL).
        then().
            statusCode(OK.code).
            body("_embedded", notNullValue()).
            body(embeddedPath, notNullValue()).
            body(embeddedPath+".id", hasItems(id0, id1, id2)).
            body(embeddedPath+".name", hasItems(json0.get("name"), json1.get("name"), json2.get("name"))).
            body(embeddedPath+".password", hasItems(json0.get("password"), json1.get("password"), json2.get("password"))).
            body(embeddedPath+"."+EDIT_LINK_PATH, hasItems(editLink(id0), editLink(id1), editLink(id2))).

            
            body("_links", notNullValue()).
            body("_links.first", nullValue()).

            body("_links.prev", nullValue()).

            body("_links.self", notNullValue()).
            body("_links.self.href", equalTo(paginationLink(0, 5))).

            body("_links.next", nullValue()).

            body("_links.last", nullValue()).

            body("_links.create", notNullValue()).
            body("_links.create.href", equalTo(API_URL)).


            body("page", notNullValue()).
            body("page.size", equalTo(5)).
            body("page.totalElements", equalTo(3)).
            body("page.totalPages", equalTo(1)).
            body("page.number", equalTo(0));
    }

    @Test
    public void testFindEmpty() {
        when().
            get(API_URL).
        then().
            statusCode(OK.code).
            body("_embedded", nullValue()).
            
            body("_links", notNullValue()).
            body("_links.first", nullValue()).

            body("_links.prev", nullValue()).

            body("_links.self", notNullValue()).
            body("_links.self.href", equalTo(paginationLink(0, 5))).

            body("_links.next", nullValue()).

            body("_links.last", nullValue()).

            body("_links.create", notNullValue()).
            body("_links.create.href", equalTo(API_URL)).


            body("page", notNullValue()).
            body("page.size", equalTo(5)).
            body("page.totalElements", equalTo(0)).
            body("page.totalPages", equalTo(0)).
            body("page.number", equalTo(0));
    }

    @Test 
    public void testFindSorted() {
        JSONObject json0 = new JSONObject()
            .put("name", "Integration Test 0")
            .put("password", "0000");

        JSONObject json1 = new JSONObject()
            .put("name", "Integration Test 1")
            .put("password", "1111");

        JSONObject json2 = new JSONObject()
            .put("name", "Integration Test 2")
            .put("password", "2222");

        String id0 = insertOne(json0);
        String id1 = insertOne(json1);
        String id2 = insertOne(json2);

        String embeddedPath = "_embedded."+EMBEDDED_DATA_NAME;

        when().
            get(API_URL + "?sort=name,desc").
        then().
            statusCode(OK.code).
            body("_embedded", notNullValue()).
            body(embeddedPath, notNullValue()).
            body(embeddedPath+".id", equalTo(List.of(id2, id1, id0))).
            body(embeddedPath+".name", equalTo(List.of(json2.get("name"), json1.get("name"), json0.get("name")))).
            body(embeddedPath+".password", equalTo(List.of(json2.get("password"), json1.get("password"), json0.get("password")))).
            body(embeddedPath+"."+EDIT_LINK_PATH, equalTo(List.of(editLink(id2), editLink(id1), editLink(id0)))).

            
            body("_links", notNullValue()).
            body("_links.first", nullValue()).

            body("_links.prev", nullValue()).

            body("_links.self", notNullValue()).
            body("_links.self.href", equalTo(paginationSortLink(0, 5, "name,desc"))).

            body("_links.next", nullValue()).

            body("_links.last", nullValue()).

            body("_links.create", notNullValue()).
            body("_links.create.href", equalTo(API_URL)).


            body("page", notNullValue()).
            body("page.size", equalTo(5)).
            body("page.totalElements", equalTo(3)).
            body("page.totalPages", equalTo(1)).
            body("page.number", equalTo(0));

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

    private String paginationLink(int page, int size) {
        return API_URL + "?page=" + page + "&size=" + size;
    }

    private String paginationSortLink(int page, int size, String sort) {
        return paginationLink(page, size) + "&sort=" + sort;
    }    

}
